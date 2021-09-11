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

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import javax.inject.Inject;
import net.runelite.api.ItemID;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

public class CoalBagOverlay extends WidgetItemOverlay
{
	private static final Collection<Integer> COAL_BAG_IDS = ImmutableList.of(
		ItemID.OPEN_COAL_BAG,
		ItemID.COAL_BAG,
		ItemID.COAL_BAG_12019,
		ItemID.COAL_BAG_25627
	);

	@Inject
	CoalBagOverlay() { showOnInventory(); }

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget)
	{

		if (COAL_BAG_IDS.contains(itemId))
		{
			final Rectangle bounds = itemWidget.getCanvasBounds();
			final TextComponent textComponent = new TextComponent();
			textComponent.setPosition(new Point(bounds.x - 1, bounds.y + 8));

			if (CoalInBag.isUnknown()) {
				textComponent.setColor(Color.GRAY);
				textComponent.setText("?");
			} else if (CoalInBag.isEmpty()) {
				textComponent.setColor(Color.RED);
				textComponent.setText("0");
			} else {
				textComponent.setColor(Color.CYAN);
				textComponent.setText(CoalInBag.tellAmount());
			}

			textComponent.render(graphics);
		}
	}
}
