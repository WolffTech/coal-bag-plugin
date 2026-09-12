package com.coalbagplugin;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoalBagPluginIntegrationTest
{
	private final Map<Integer, ItemContainer> containers = new HashMap<>();
	private final Map<Integer, Widget> widgets = new HashMap<>();
	private CoalBag coalBag;
	private CoalBagPlugin plugin;

	@Before
	public void setUp() throws ReflectiveOperationException
	{
		coalBag = new CoalBag();
		plugin = new CoalBagPlugin();
		setField(plugin, "coalBag", coalBag);
		setField(plugin, "client", createClient());
	}

	@Test
	public void loginTransitionsInvalidateKnownAmount()
	{
		for (GameState gameState : new GameState[]{GameState.LOGGING_IN, GameState.LOGIN_SCREEN})
		{
			setKnownAmount(10);

			GameStateChanged event = new GameStateChanged();
			event.setGameState(gameState);
			plugin.onGameStateChanged(event);

			assertTrue(coalBag.isUnknown());
		}
	}

	@Test
	public void loggedInStatePreservesKnownAmount()
	{
		setKnownAmount(10);
		GameStateChanged event = new GameStateChanged();
		event.setGameState(GameState.LOGGED_IN);

		plugin.onGameStateChanged(event);

		assertEquals("10", coalBag.getAmount());
	}

	@Test
	public void inventoryRemovalInvalidatesKnownAmount()
	{
		setKnownAmount(10);

		plugin.onItemContainerChanged(new ItemContainerChanged(
				InventoryID.INV, itemContainer(ItemID.COAL_BAG)));
		assertEquals("10", coalBag.getAmount());

		plugin.onItemContainerChanged(new ItemContainerChanged(
				InventoryID.INV, itemContainer(ItemID.COAL_BAG_OPEN)));
		assertEquals("10", coalBag.getAmount());

		plugin.onItemContainerChanged(new ItemContainerChanged(
				InventoryID.WORN, itemContainer()));
		assertEquals("10", coalBag.getAmount());

		plugin.onItemContainerChanged(new ItemContainerChanged(
				InventoryID.INV, itemContainer()));
		assertTrue(coalBag.isUnknown());
	}

	@Test
	public void destroyClickPreservesAmountUntilBagIsRemoved()
	{
		setKnownAmount(10);

		plugin.onMenuOptionClicked(menuClick("Destroy", "<col=ff9040>Coal bag</col>"));

		assertEquals("10", coalBag.getAmount());

		plugin.onItemContainerChanged(new ItemContainerChanged(
				InventoryID.INV, itemContainer()));
		assertTrue(coalBag.isUnknown());
	}

	@Test
	public void inventoryRemovalClearsPendingBonusOre()
	{
		containers.put(InventoryID.INV, itemContainer(ItemID.COAL_BAG_OPEN));
		setKnownAmount(10);
		plugin.onMenuOptionClicked(menuClick("Mine", "Coal rocks"));

		plugin.onItemContainerChanged(new ItemContainerChanged(
				InventoryID.INV, itemContainer()));
		containers.put(InventoryID.INV, itemContainer(ItemID.COAL_BAG_OPEN));
		setKnownAmount(10);
		plugin.onChatMessage(chatMessage(
				"Your Celestial ring allows you to mine an additional ore."));

		assertEquals("10", coalBag.getAmount());
	}

	@Test
	public void minedCoalAndBonusOreRespectStandardCapacity()
	{
		containers.put(InventoryID.INV, itemContainer(ItemID.COAL_BAG_OPEN));
		containers.put(InventoryID.WORN, itemContainer());
		setKnownAmount(26);

		plugin.onChatMessage(chatMessage("You manage to mine some coal."));
		plugin.onChatMessage(chatMessage(
				"The Varrock platebody enabled you to mine an additional ore."));

		assertEquals("27", coalBag.getAmount());
	}

	@Test
	public void smithingAndMaxCapesIncreaseCapacity()
	{
		for (int cape : new int[]{
				ItemID.SKILLCAPE_SMITHING,
				ItemID.SKILLCAPE_SMITHING_TRIMMED,
				ItemID.SKILLCAPE_MAX,
				ItemID.SKILLCAPE_MAX_WORN})
		{
			containers.put(InventoryID.INV, itemContainer(ItemID.COAL_BAG_OPEN));
			containers.put(InventoryID.WORN, itemContainer(cape));
			setKnownAmount(35);

			plugin.onChatMessage(chatMessage("You manage to mine some coal."));

			assertEquals("36", coalBag.getAmount());
		}
	}

	@Test
	public void chatMessagesAreNormalizedBeforeParsing()
	{
		containers.put(InventoryID.INV, itemContainer(ItemID.COAL_BAG));

		plugin.onChatMessage(chatMessage(
				"<col=ff0000>The coal bag contains<br><u>12</u> pieces of coal.</col>"));

		assertEquals("12", coalBag.getAmount());
	}

	@Test
	public void widgetMessageUsesNamedObjectboxComponent()
	{
		widgets.put(InterfaceID.Objectbox.TEXT,
				widget("The coal bag contains 8 pieces of coal."));

		plugin.onClientTick(new ClientTick());

		assertEquals("8", coalBag.getAmount());
	}

	@Test
	public void bankContainerMessageRequiresCoalBagInInventory()
	{
		containers.put(InventoryID.INV, itemContainer());
		setKnownAmount(10);

		plugin.onChatMessage(chatMessage(
				"You empty all of your containers into the bank."));

		assertEquals("10", coalBag.getAmount());
		assertFalse(coalBag.isEmpty());
	}

	private void setKnownAmount(int amount)
	{
		coalBag.updateAmount("The coal bag contains " + amount + " pieces of coal.");
	}

	private Client createClient()
	{
		return proxy(Client.class, (methodName, arguments) ->
		{
			if ("getItemContainer".equals(methodName) && arguments[0] instanceof Integer)
			{
				return containers.get(arguments[0]);
			}
			if ("getWidget".equals(methodName) && arguments[0] instanceof Integer)
			{
				return widgets.get(arguments[0]);
			}
			return null;
		});
	}

	private static ItemContainer itemContainer(int... itemIds)
	{
		Set<Integer> ids = Arrays.stream(itemIds).boxed().collect(Collectors.toSet());
		return proxy(ItemContainer.class, (methodName, arguments) ->
		{
			if ("contains".equals(methodName))
			{
				return ids.contains(arguments[0]);
			}
			return null;
		});
	}

	private static Widget widget(String text)
	{
		return proxy(Widget.class, (methodName, arguments) ->
				"getText".equals(methodName) ? text : null);
	}

	private static ChatMessage chatMessage(String message)
	{
		ChatMessage event = new ChatMessage();
		event.setType(ChatMessageType.GAMEMESSAGE);
		event.setMessage(message);
		return event;
	}

	private static MenuOptionClicked menuClick(String option, String target)
	{
		MenuEntry menuEntry = proxy(MenuEntry.class, (methodName, arguments) ->
		{
			if ("getOption".equals(methodName))
			{
				return option;
			}
			if ("getTarget".equals(methodName))
			{
				return target;
			}
			return null;
		});
		return new MenuOptionClicked(menuEntry);
	}

	private static void setField(Object target, String fieldName, Object value)
			throws ReflectiveOperationException
	{
		Field field = target.getClass().getDeclaredField(fieldName);
		field.setAccessible(true);
		field.set(target, value);
	}

	private static <T> T proxy(Class<T> type, Invocation invocation)
	{
		return type.cast(Proxy.newProxyInstance(
				type.getClassLoader(),
				new Class<?>[]{type},
				(proxy, method, arguments) ->
				{
					Object result = invocation.invoke(
							method.getName(), arguments == null ? new Object[0] : arguments);
					return result != null ? result : defaultValue(method.getReturnType());
				}));
	}

	private static Object defaultValue(Class<?> type)
	{
		if (!type.isPrimitive())
		{
			return null;
		}
		if (type == boolean.class)
		{
			return false;
		}
		if (type == char.class)
		{
			return '\0';
		}
		if (type == byte.class)
		{
			return (byte) 0;
		}
		if (type == short.class)
		{
			return (short) 0;
		}
		if (type == int.class)
		{
			return 0;
		}
		if (type == long.class)
		{
			return 0L;
		}
		if (type == float.class)
		{
			return 0F;
		}
		if (type == double.class)
		{
			return 0D;
		}
		throw new IllegalArgumentException("Unsupported primitive type: " + type);
	}

	@FunctionalInterface
	private interface Invocation
	{
		Object invoke(String methodName, Object[] arguments);
	}
}
