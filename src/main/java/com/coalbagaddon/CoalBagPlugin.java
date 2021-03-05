package com.coalbagaddon;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Coal Bag",
	description = "Shows how much coal is in the coal bag.",
	tags = {"coal", "bag"}

)
public class CoalBagPlugin extends Plugin
{
	private static final Pattern BAG_EMPTY_MESSAGE = Pattern.compile("^The coal bag is empty\\.$");
	private static final Pattern BAG_ONE_MESSAGE = Pattern.compile("^The coal bag contains one piece of coal\\.$");
	private static final Pattern BAG_MANY_MESSAGE = Pattern.compile("^The coal bag contains ([\\d]+)? pieces? of coal\\.$");

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CoalBagOverlay coalBagOverlay;

	@Override
	protected void startUp()
	{
		overlayManager.add(coalBagOverlay);
		CoalInBag.updateAmount(-1);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(coalBagOverlay);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE)
		{
			return;
		}

		Matcher matcher = BAG_ONE_MESSAGE.matcher(event.getMessage());
		if (matcher.matches())
		{
			CoalInBag.updateAmount(1);
		}

		Matcher matcher2 = BAG_MANY_MESSAGE.matcher(event.getMessage());
		if (matcher2.matches())
		{
			final int num = Integer.parseInt(matcher2.group(1));
			CoalInBag.updateAmount((num));
		}

		Matcher matcher3 = BAG_EMPTY_MESSAGE.matcher(event.getMessage());
		if (matcher3.matches())
		{
			CoalInBag.updateAmount(0);
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		switch (event.getMenuOption())
		{
			case "Empty":
				CoalInBag.updateAmount(0);
				break;
			case "Destroy":
				CoalInBag.updateAmount(-1);
				break;
			default:
				break;
		}
	}

}
