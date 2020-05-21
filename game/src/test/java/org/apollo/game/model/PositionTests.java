package org.apollo.game.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link Position} class.
 */
public final class PositionTests {

	/**
	 * Tests the {@link Position#getHeight()} method.
	 */
	@Test
	public void getHeight() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(3, position.getHeight());
	}

	/**
	 * Tests the creation of an invalid {@link Position}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void createNegativePosition() {
		new Position(3222, 3222, -1);
	}

	/**
	 * Tests the creation of an invalid {@link Position}.
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void createOutOfBoundsPosition() {
		new Position(3222, 3222, 4);
	}

}