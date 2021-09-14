/*
 * Copyright (c) 2019 Adam <Adam@sigterm.info>
 * Copyright (c) 2021 Nick Wolff <nick@wolff.tech>
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
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(
		name = "Coal Bag",
		description = "Shows how much coal is in the coal bag.",
		tags = {"coal", "bag"}
)
public class CoalBagPlugin extends Plugin
{

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CoalBagOverlay coalBagOverlay;

	@Provides
	CoalBagConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CoalBagConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(coalBagOverlay);
		CoalBag.setUnknownAmount();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(coalBagOverlay);
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		if (event.getType() == ChatMessageType.GAMEMESSAGE)
		{
			CoalBag.updateAmount(event.getMessage());
		}
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		// because the coal bag sometimes displays the emptied amount message as a widget, we need to check for that here
		Widget coalBagWidget = client.getWidget(12648450);
		if (coalBagWidget != null)
		{
			CoalBag.updateAmount(coalBagWidget.getText());
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if ("Destroy".equals(event.getMenuOption()))
		{
			CoalBag.setUnknownAmount();
		}
	}
}
