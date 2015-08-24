package org.apollo.game.model;

/**
 * Represents a single movement direction.
 *
 * @author Graham
 */
public enum Direction {

	/**
	 * No movement.
	 */
	NONE(-1),

	/**
	 * North west movement.
	 */
	NORTH_WEST(0),

	/**
	 * North movement.
	 */
	NORTH(1),

	/**
	 * North east movement.
	 */
	NORTH_EAST(2),

	/**
	 * West movement.
	 */
	WEST(3),

	/**
	 * East movement.
	 */
	EAST(4),

	/**
	 * South west movement.
	 */
	SOUTH_WEST(5),

	/**
	 * South movement.
	 */
	SOUTH(6),

	/**
	 * South east movement.
	 */
	SOUTH_EAST(7);

	/**
	 * An empty direction array.
	 */
	public static final Direction[] EMPTY_DIRECTION_ARRAY = new Direction[0];

	/**
	 * Gets the Direction between the two {@link Position}s..
	 *
	 * @param current The difference between two X coordinates.
	 * @param next The difference between two Y coordinates.
	 * @return The direction.
	 */
	public static Direction between(Position current, Position next) {
		int deltaX = next.getX() - current.getX();
		int deltaY = next.getY() - current.getY();

		if (deltaY == 1) {
			if (deltaX == 1) {
				return NORTH_EAST;
			} else if (deltaX == 0) {
				return NORTH;
			} else if (deltaX == -1) {
				return NORTH_WEST;
			}
		} else if (deltaY == -1) {
			if (deltaX == 1) {
				return SOUTH_EAST;
			} else if (deltaX == 0) {
				return SOUTH;
			} else if (deltaX == -1) {
				return SOUTH_WEST;
			}
		} else if (deltaY == 0) {
			if (deltaX == 1) {
				return EAST;
			} else if (deltaX == 0) {
				return NONE;
			} else if (deltaX == -1) {
				return WEST;
			}
		}

		throw new IllegalArgumentException("Difference between Positions must be [-1, 1].");
	}

	/**
	 * The direction as an integer.
	 */
	private final int intValue;

	/**
	 * Creates the direction.
	 *
	 * @param intValue The direction as an integer.
	 */
	Direction(int intValue) {
		this.intValue = intValue;
	}

	/**
	 * Gets the direction as an integer which the client can understand.
	 *
	 * @return The movement as an integer.
	 */
	public int toInteger() {
		return intValue;
	}

}