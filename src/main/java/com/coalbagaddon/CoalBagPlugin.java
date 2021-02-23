package com.coalbagaddon;

import com.google.common.collect.ImmutableMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "CoalBag Addon",
	description = "Shows how much coal is in the coal bag.",
	tags = {"coal", "bag", "addon"}

)
public class CoalBagPlugin extends Plugin
{
	private static final Pattern BAG_ONE_MESSAGE = Pattern.compile("^The coal bag contains one piece of coal\\.$");
	private static final Pattern BAG_MANY_MESSAGE = Pattern.compile("^The coal bag contains ([\\d]+)? pieces? of coal\\.$");
	private static final ImmutableMap<String, Integer> GRAB_NUMBER = ImmutableMap.<String, Integer>builder()
		.put("2", 2)
		.put("3", 3)
		.put("4", 4)
		.put("5", 5)
		.put("6", 6)
		.put("7", 7)
		.put("8", 8)
		.put("9", 9)
		.put("10", 10)
		.put("11", 11)
		.put("12", 12)
		.put("13", 13)
		.put("14", 14)
		.put("15", 15)
		.put("16", 16)
		.put("17", 17)
		.put("18", 18)
		.put("19", 19)
		.put("20", 20)
		.put("21", 21)
		.put("22", 22)
		.put("23", 23)
		.put("24", 24)
		.put("25", 25)
		.put("26", 26)
		.put("27", 27)
		.put("28", 28)
		.put("29", 29)
		.put("30", 30)
		.put("31", 31)
		.put("32", 32)
		.put("33", 33)
		.put("34", 34)
		.put("35", 35)
		.put("36", 36)
		.build();

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
		log.info("CoalBag started!");
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
			final int num = GRAB_NUMBER.get(matcher2.group(1));
			CoalInBag.updateAmount((num));
		}
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(coalBagOverlay);
		log.info("CoalBag stopped!");
	}
}
