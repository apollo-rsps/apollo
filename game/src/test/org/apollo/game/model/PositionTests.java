package org.apollo.game.model;

import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.area.Region;

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
	 * Tests the {@link Position#getX()} method.
	 */
	@Test
	public void getX() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(3222, position.getX());
	}

	/**
	 * Tests the {@link Position#getY()} method.
	 */
	@Test
	public void getY() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(3222, position.getY());
	}

	/**
	 * Tests the {@link Position#getCentralRegionX()} method.
	 */
	@Test
	public void getCentralRegionX() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(402,75, position.getCentralRegionX());
	}

	/**
	 * Tests the {@link Position#getCentralRegionY()} method.
	 */
	@Test
	public void getCentralRegionY() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(402,75, position.getCentralRegionY());
	}

	/**
	 * Tests the {@link Position#getDistance()} method.
	 */
	@Test
	public void getDistance() {
		Position position1 = new Position(3222, 3222, 3);
		Position position2 = new Position(3220, 3220, 3);
		Assert.assertEquals(3, position1.getDistance(position2));
	}

	/**
	 * Tests the {@link Position#getLocalX()} method.
	 */
	@Test
	public void getLocalX() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(54, position.getLocalX());
	}

	/**
	 * Tests the {@link Position#getLocalY()} method.
	 */
	@Test
	public void getLocalY() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(54, position.getLocalY());
	}

	/**
	 * Tests the {@link Position#getLongestDelta()} method.
	 */
	@Test
	public void getLongestDelta() {
		Position position1 = new Position(3222, 3222, 3);
		Position position2 = new Position(3219, 3220, 3);
		Assert.assertEquals(3, position1.getLongestDelta(position2));
	}

	/**
	 * Tests the {@link Position#getRegionCoordinates()} method.
	 */
	@Test
	public void getRegionCoordinates() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertEquals(new RegionCoordinates(396,396), position.getRegionCoordinates());
	}

	/**
	 * Tests the {@link Position#inside()} method.
	 */
	@Test
	public void inside() {
		Position position = new Position(3222, 3222, 3);
		Assert.assertTrue(position.inside(new Region(396,396)));
		Assert.assertFalse(position.inside(new Region(1,1)));
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