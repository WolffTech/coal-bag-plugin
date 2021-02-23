package com.coalbagaddon;

import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
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
	private static final int INVENTORY_SIZE = 28;

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
		CoalInBag.UpdateAmount(-1);
		log.info("CoalBag started!");
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(coalBagOverlay);
		log.info("CoalBag stopped!");
	}
}
