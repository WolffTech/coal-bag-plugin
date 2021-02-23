package com.coalbagaddon;

public class CoalInBag
{
	private static int storedAmount;

	public static String tellAmount()
	{
		return String.valueOf(storedAmount);
	}

	public static void updateAmount(int stored)
	{
		storedAmount = stored;
	}

	public static boolean isEmpty()
	{
		return storedAmount == 0;
	}

	public static boolean isUnknown()
	{
		return storedAmount < 0;
	}
}
