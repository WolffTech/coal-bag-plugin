package com.coalbagaddon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.ui.overlay.WidgetItemOverlay;
import net.runelite.client.ui.overlay.components.TextComponent;

public class CoalBagOverlay extends WidgetItemOverlay
{
	@Inject
	CoalBagOverlay() { showOnInventory(); }

	@Override
	public void renderItemOverlay(Graphics2D graphics, int itemId, WidgetItem itemWidget)
	{
		final Rectangle bounds = itemWidget.getCanvasBounds();
		final TextComponent textComponent = new TextComponent();
		textComponent.setPosition(new Point(bounds.x -1, bounds.y + 8));
		textComponent.setColor(Color.CYAN);

		if (CoalInBag.isUnknown())
		{
			textComponent.setText("?");
		}
		else
		{
			textComponent.setText(CoalInBag.tellAmount());
		}
		textComponent.render(graphics);
	}
}
