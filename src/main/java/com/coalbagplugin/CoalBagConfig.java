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

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup("coalbag")
public interface CoalBagConfig extends Config
{
	@Alpha
	@ConfigItem(
			keyName = "knownCoalBagColor",
			name = "Filled Color",
			description = "Configures the text color when there is an known quantity of coal in the coal bag",
			position = 1
	)
	default Color knownCoalBagColor()
	{
		return Color.CYAN;
	}

	@Alpha
	@ConfigItem(
			keyName = "emptyCoalBagColor",
			name = "Empty Color",
			description = "Configures the text color when 0 coal is in the coal bag",
			position = 2
	)
	default Color emptyCoalBagColor()
	{
		return Color.RED;
	}

	@Alpha
	@ConfigItem(
			keyName = "unknownCoalBagColor",
			name = "Unknown Color",
			description = "Configures the text color when there is an unknown quantity of coal in the coal bag",
			position = 3
	)
	default Color unknownCoalBagColor()
	{
		return Color.GRAY;
	}
}