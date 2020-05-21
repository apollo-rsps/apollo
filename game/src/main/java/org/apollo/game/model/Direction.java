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
	 * An array of directions without any diagonal directions.
	 */
	public final static Direction[] NESW = { NORTH, EAST, SOUTH, WEST };

	/**
	 * An array of directions without any diagonal directions, and one step counter-clockwise, as used by
	 * the clients collision mapping.
	 */
	public final static Direction[] WNES = { WEST, NORTH, EAST, SOUTH };

	/**
	 * An array of diagonal directions, and one step counter-clockwise, as used by the clients collision
	 * mapping.
	 */
	public final static Direction[] WNES_DIAGONAL = { NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST};

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

		return fromDeltas(Integer.signum(deltaX), Integer.signum(deltaY));
	}

	/**
	 * Creates a direction from the differences between X and Y.
	 *
	 * @param deltaX The difference between two X coordinates.
	 * @param deltaY The difference between two Y coordinates.
	 * @return The direction.
	 */
	public static Direction fromDeltas(int deltaX, int deltaY) {
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
	 * Get the 2 directions which make up a diagonal direction (i.e., NORTH and EAST for NORTH_EAST).
	 *
	 * @param direction The direction to get the components for.
	 * @return The components for the given direction.
	 */
	public static Direction[] diagonalComponents(Direction direction) {
		switch (direction) {
			case NORTH_EAST:
				return new Direction[] { NORTH, EAST };
			case NORTH_WEST:
				return new Direction[] { NORTH, WEST };
			case SOUTH_EAST:
				return new Direction[] { SOUTH, EAST };
			case SOUTH_WEST:
				return new Direction[] { SOUTH, WEST };
		}

		throw new IllegalArgumentException("Must provide a diagonal direction.");
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
	 * Gets the opposite direction of the this direction.
	 *
	 * @return The opposite direction.
	 */
	public Direction opposite() {
		switch (this) {
			case NORTH:
				return SOUTH;
			case SOUTH:
				return NORTH;
			case EAST:
				return WEST;
			case WEST:
				return EAST;
			case NORTH_WEST:
				return SOUTH_EAST;
			case NORTH_EAST:
				return SOUTH_WEST;
			case SOUTH_EAST:
				return NORTH_WEST;
			case SOUTH_WEST:
				return NORTH_EAST;
		}

		return NONE;
	}

	/**
	 * Gets the X delta from a {@link Position} of (0, 0).
	 *
	 * @return The delta of X from (0, 0).
     */
	public int deltaX() {
		switch (this) {
			case SOUTH_EAST:
			case NORTH_EAST:
			case EAST:
				return 1;
			case SOUTH_WEST:
			case NORTH_WEST:
			case WEST:
				return -1;
		}

		return 0;
	}

	/**
	 * Gets the Y delta from a {@link Position} of (0, 0).
	 *
	 * @return The delta of Y from (0, 0).
	 */
	public int deltaY() {
		switch (this) {
			case NORTH_WEST:
			case NORTH_EAST:
			case NORTH:
				return 1;
			case SOUTH_WEST:
			case SOUTH_EAST:
			case SOUTH:
				return -1;
		}

		return 0;
	}

	/**
	 * Check if this direction is a diagonal direction.
	 *
	 * @return {@code true} if this direction is a diagonal direction, {@code false} otherwise.
	 */
	public boolean isDiagonal() {
		return this == SOUTH_EAST || this == SOUTH_WEST || this == NORTH_EAST || this == NORTH_WEST;
	}

	/**
	 * Gets the direction as an integer which the client can understand.
	 *
	 * @return The movement as an integer.
	 */
	public int toInteger() {
		return intValue;
	}

	/**
	 * Gets the direction as an integer as used orientation in the client maps (WNES as opposed to NESW).
	 *
	 * @return The direction as an integer.
	 */
	public int toOrientationInteger() {
		switch(this) {
			case WEST:
			case NORTH_WEST:
				return 0;
			case NORTH:
			case NORTH_EAST:
				return 1;
			case EAST:
			case SOUTH_EAST:
				return 2;
			case SOUTH:
			case SOUTH_WEST:
				return 3;
			default:
				throw new IllegalStateException("Only a valid direction can have an orientation value");
		}

	}
}