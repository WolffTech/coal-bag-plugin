/*
 * Copyright (c) 2019 Adam <Adam@sigterm.info>
 * Copyright (c) 2021 Nick Wolff <nick@wolff.tech>
 * Copyright (c) 2022 TicTac7x
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.coalbagplugin;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ItemContainer;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.gameval.InventoryID;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

import javax.inject.Inject;
import java.util.regex.Pattern;

@Slf4j
@PluginDescriptor(
		name = "Coal Bag",
		description = "Shows how much coal is in the coal bag.",
		tags = {"coal", "bag"}
)
public class CoalBagPlugin extends Plugin
{
	private static final int COAL_BAG_CAPACITY = 27;
	private static final int SMITHING_CAPE_COAL_BAG_CAPACITY = 36;

	private static final String EMPTY_ALL_CONTAINERS_MESSAGE = "You empty all of your containers into the bank.";
	private static final String MINE_OPTION = "Mine";
	private static final String COAL_ROCKS_TARGET = "Coal rocks";
	private static final String COAL_MINED_MESSAGE = "You manage to mine some coal.";

	private static final Pattern BONUS_ORE_MESSAGE = Pattern.compile(
			"^(?:Your Celestial ring allows you to mine an additional ore\\.|The Varrock platebody enabled you to mine an additional ore\\.)$"
	);

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CoalBagOverlay coalBagOverlay;

	@Inject
	private CoalBag coalBag;

	private boolean miningCoal;

	@Provides
	CoalBagConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CoalBagConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(coalBagOverlay);
		resetTracking();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(coalBagOverlay);
		resetTracking();
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGING_IN
				|| event.getGameState() == GameState.LOGIN_SCREEN)
		{
			resetTracking();
		}
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() == InventoryID.INV && !hasCoalBag(event.getItemContainer()))
		{
			resetTracking();
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (!isTrackedChatMessageType(event.getType()))
		{
			return;
		}

		String message = cleanText(event.getMessage());
		if (!EMPTY_ALL_CONTAINERS_MESSAGE.equals(message) || hasCoalBag())
		{
			coalBag.updateAmount(message);
		}

		if (hasOpenCoalBag() && COAL_MINED_MESSAGE.equals(message))
		{
			coalBag.addAmount(1, getCoalBagCapacity());
			miningCoal = true;
		}
		else if (miningCoal && hasOpenCoalBag() && BONUS_ORE_MESSAGE.matcher(message).matches())
		{
			coalBag.addAmount(1, getCoalBagCapacity());
		}
	}

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		// running this under onClientTick as it is possible to close the widget on the same tick that it opens.
		// because the coal bag sometimes displays the emptied amount message as a widget, we need to check for that here.
		Widget coalBagWidget = client.getWidget(InterfaceID.Objectbox.TEXT);
		if (coalBagWidget != null)
		{
			coalBag.updateAmount(cleanText(coalBagWidget.getText()));
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		String option = cleanText(event.getMenuOption());
		String target = cleanText(event.getMenuTarget());

		miningCoal = MINE_OPTION.equals(option) && target.contains(COAL_ROCKS_TARGET);
	}

	private static boolean isTrackedChatMessageType(ChatMessageType type)
	{
		return type == ChatMessageType.GAMEMESSAGE
				|| type == ChatMessageType.SPAM;
	}

	private static String cleanText(String text)
	{
		if (text == null)
		{
			return "";
		}

		return Text.removeTags(text.replace("<br>", " "))
				.replace('\u00A0', ' ')
				.trim();
	}

	private boolean hasCoalBag()
	{
		return hasCoalBag(client.getItemContainer(InventoryID.INV));
	}

	private static boolean hasCoalBag(ItemContainer inventory)
	{
		return inventory != null
				&& (inventory.contains(ItemID.COAL_BAG) || inventory.contains(ItemID.COAL_BAG_OPEN));
	}

	private boolean hasOpenCoalBag()
	{
		ItemContainer inventory = client.getItemContainer(InventoryID.INV);
		return inventory != null && inventory.contains(ItemID.COAL_BAG_OPEN);
	}

	private int getCoalBagCapacity()
	{
		ItemContainer equipment = client.getItemContainer(InventoryID.WORN);
		boolean smithingCapeEquipped = equipment != null
				&& (equipment.contains(ItemID.SKILLCAPE_SMITHING)
				|| equipment.contains(ItemID.SKILLCAPE_SMITHING_TRIMMED)
				|| equipment.contains(ItemID.SKILLCAPE_MAX)
				|| equipment.contains(ItemID.SKILLCAPE_MAX_WORN));
		return smithingCapeEquipped ? SMITHING_CAPE_COAL_BAG_CAPACITY : COAL_BAG_CAPACITY;
	}

	private void resetTracking()
	{
		coalBag.setUnknownAmount();
		miningCoal = false;
	}
}
