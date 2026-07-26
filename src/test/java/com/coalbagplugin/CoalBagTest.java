package com.coalbagplugin;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoalBagTest
{
	@Before
	public void setUp()
	{
		CoalBag.setUnknownAmount();
	}

	@Test
	public void updateAmountParsesCoalBagMessages()
	{
		CoalBag.updateAmount("The coal bag is empty.");
		assertTrue(CoalBag.isEmpty());

		CoalBag.updateAmount("The coal bag is now empty.");
		assertTrue(CoalBag.isEmpty());

		CoalBag.updateAmount("The coal bag contains one piece of coal.");
		assertEquals("1", CoalBag.getAmount());

		CoalBag.updateAmount("The coal bag still contains one piece of coal.");
		assertEquals("1", CoalBag.getAmount());

		CoalBag.updateAmount("The coal bag contains 27 pieces of coal.");
		assertEquals("27", CoalBag.getAmount());
	}

	@Test
	public void updateAmountParsesExactEmptyAllContainersMessage()
	{
		CoalBag.updateAmount("The coal bag contains 10 pieces of coal.");
		CoalBag.updateAmount("You empty all of your containers into the bank.");
		assertTrue(CoalBag.isEmpty());

		CoalBag.updateAmount("The coal bag contains 10 pieces of coal.");
		CoalBag.updateAmount("You empty all of your containers into the bank. Extra text");
		assertEquals("10", CoalBag.getAmount());
	}

	@Test
	public void addAmountPreservesUnknownAndClampsWithoutReducing()
	{
		CoalBag.addAmount(1, 27);
		assertTrue(CoalBag.isUnknown());

		CoalBag.updateAmount("The coal bag contains 20 pieces of coal.");
		CoalBag.addAmount(10, 27);
		assertEquals("27", CoalBag.getAmount());

		CoalBag.updateAmount("The coal bag contains 30 pieces of coal.");
		CoalBag.addAmount(1, 27);
		assertEquals("30", CoalBag.getAmount());

		CoalBag.addAmount(-1, 27);
		assertEquals("30", CoalBag.getAmount());
	}

}
