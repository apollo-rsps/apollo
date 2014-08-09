package org.apollo.game.model;

import com.google.common.base.Objects;

/**
 * Represents a position in the world.
 * 
 * @author Graham
 */
public final class Position {

	/**
	 * The number of height levels.
	 */
	public static final int HEIGHT_LEVELS = 4;

	/**
	 * The maximum distance players/NPCs can 'see'.
	 */
	public static final int MAX_DISTANCE = 15;

	/**
	 * The height level.
	 */
	private final int height;

	/**
	 * The x coordinate.
	 */
	private final int x;

	/**
	 * The y coordinate.
	 */
	private final int y;

	/**
	 * Creates a position at the default height.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Creates a position with the specified height.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param height The height.
	 */
	public Position(int x, int y, int height) {
		if (height < 0 || height >= HEIGHT_LEVELS) {
			throw new IllegalArgumentException("Height level out of bounds.");
		}
		this.x = x;
		this.y = y;
		this.height = height;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		} else if (this == obj) {
			return true;
		} else if (getClass() != obj.getClass()) {
			return false;
		}

		Position other = (Position) obj;
		return x == other.x && y == other.y && height == other.height;
	}

	/**
	 * Gets the x coordinate of the central sector.
	 * 
	 * @return The x coordinate of the central sector.
	 */
	public int getCentralSectorX() {
		return x / 8;
	}

	/**
	 * Gets the y coordinate of the central sector.
	 * 
	 * @return The y coordinate of the central sector.
	 */
	public int getCentralSectorY() {
		return y / 8;
	}

	/**
	 * Gets the distance between this position and another position. Only x and y are considered (i.e. 2 dimensions).
	 * 
	 * @param other The other position.
	 * @return The distance.
	 */
	public int getDistance(Position other) {
		int deltaX = x - other.x;
		int deltaY = y - other.y;
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}

	/**
	 * Gets the height level.
	 * 
	 * @return The height level.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the x coordinate inside the sector of this position.
	 * 
	 * @return The local x coordinate.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local x coordinate inside the sector of the {@code base} position.
	 * 
	 * @param base The base position.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Position base) {
		return x - base.getTopLeftSectorX() * 8;
	}

	/**
	 * Gets the y coordinate inside the sector of this position.
	 * 
	 * @return The local y coordinate.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local y coordinate inside the sector of the {@code base} position.
	 * 
	 * @param base The base position.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Position base) {
		return y - base.getTopLeftSectorY() * 8;
	}

	/**
	 * Gets the longest horizontal or vertical delta between the two positions.
	 * 
	 * @param other The other position.
	 * @return The longest horizontal or vertical delta.
	 */
	public int getLongestDelta(Position other) {
		int deltaX = Math.abs(x - other.x);
		int deltaY = Math.abs(y - other.y);
		return Math.max(deltaX, deltaY);
	}

	/**
	 * Gets the x coordinate of the sector this position is in.
	 * 
	 * @return The sector x coordinate.
	 */
	public int getTopLeftSectorX() {
		return x / 8 - 6;
	}

	/**
	 * Gets the y coordinate of the sector this position is in.
	 * 
	 * @return The sector y coordinate.
	 */
	public int getTopLeftSectorY() {
		return y / 8 - 6;
	}

	/**
	 * Gets the x coordinate.
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate.
	 * 
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return height << 30 & 0xC0000000 | y << 15 & 0x3FFF8000 | x & 0x7FFF;
	}

	/**
	 * Checks if the position is within distance of another.
	 * 
	 * @param other The other position.
	 * @param distance The distance.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Position other, int distance) {
		int deltaX = Math.abs(x - other.x);
		int deltaY = Math.abs(y - other.y);
		return deltaX <= distance && deltaY <= distance;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).add("x", x).add("y", y).add("height", height)
				.add("sector x", getTopLeftSectorX()).add("sector y", getTopLeftSectorY()).toString();
	}

}