package com.coalbagaddon;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CoalBagPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CoalBagPlugin.class);
		RuneLite.main(args);
	}
}