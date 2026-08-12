package com.coalbagplugin;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CoalBagTest
{
	private CoalBag coalBag;

	@Before
	public void setUp()
	{
		coalBag = new CoalBag();
	}

	@Test
	public void updateAmountParsesCoalBagMessages()
	{
		coalBag.updateAmount("The coal bag is empty.");
		assertTrue(coalBag.isEmpty());

		coalBag.updateAmount("The coal bag is now empty.");
		assertTrue(coalBag.isEmpty());

		coalBag.updateAmount("The coal bag contains one piece of coal.");
		assertEquals("1", coalBag.getAmount());

		coalBag.updateAmount("The coal bag still contains one piece of coal.");
		assertEquals("1", coalBag.getAmount());

		coalBag.updateAmount("The coal bag contains 27 pieces of coal.");
		assertEquals("27", coalBag.getAmount());
	}

	@Test
	public void updateAmountParsesExactEmptyAllContainersMessage()
	{
		coalBag.updateAmount("The coal bag contains 10 pieces of coal.");
		coalBag.updateAmount("You empty all of your containers into the bank.");
		assertTrue(coalBag.isEmpty());

		coalBag.updateAmount("The coal bag contains 10 pieces of coal.");
		coalBag.updateAmount("You empty all of your containers into the bank. Extra text");
		assertEquals("10", coalBag.getAmount());
	}

	@Test
	public void addAmountPreservesUnknownAndClampsWithoutReducing()
	{
		coalBag.addAmount(1, 27);
		assertTrue(coalBag.isUnknown());

		coalBag.updateAmount("The coal bag contains 20 pieces of coal.");
		coalBag.addAmount(10, 27);
		assertEquals("27", coalBag.getAmount());

		coalBag.updateAmount("The coal bag contains 30 pieces of coal.");
		coalBag.addAmount(1, 27);
		assertEquals("30", coalBag.getAmount());

		coalBag.addAmount(-1, 27);
		assertEquals("30", coalBag.getAmount());
	}

	@Test
	public void instancesDoNotShareAmounts()
	{
		CoalBag otherCoalBag = new CoalBag();

		coalBag.updateAmount("The coal bag contains 10 pieces of coal.");

		assertEquals("10", coalBag.getAmount());
		assertTrue(otherCoalBag.isUnknown());
	}

}
