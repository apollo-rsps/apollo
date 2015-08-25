package org.apollo.game.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the {@link Position} class.
 * 
 * @author Ryley
 */
public final class TestPosition {

    /**
     * Tests the {@link Position#getHeight()} method.
     */
    @Test
    public void testGetHeight() {
	Position position = new Position(3222, 3222, 3);
	Assert.assertEquals(3, position.getHeight());
    }

    /**
     * Tests the creation of an invalid {@link Position}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateNegativePosition() {
	new Position(3222, 3222, -1);
    }

    /**
     * Tests the creation of an invalid {@link Position}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCreateOutOfBoundsPosition() {
	new Position(3222, 3222, 4);
    }

}