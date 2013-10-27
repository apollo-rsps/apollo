package org.apollo.game.model;

/**
 * Represents a position in the world.
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
	 * The x coordinate.
	 */
	private final int x;

	/**
	 * The y coordinate.
	 */
	private final int y;

	/**
	 * The height level.
	 */
	private final int height;

	/**
	 * Creates a position at the default height.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public Position(int x, int y) {
		this(x, y, 0);
	}

	/**
	 * Creates a position with the specified height.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param height The height.
	 */
	public Position(int x, int y, int height) {
		if (height < 0 || height >= HEIGHT_LEVELS) {
			throw new IllegalArgumentException("Height out of bounds");
		}
		this.x = x;
		this.y = y;
		this.height = height;
	}

	/**
	 * Gets the x coordinate.
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate.
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	/**
	 * Gets the height level.
	 * @return The height level.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the x coordinate of the region.
	 * @return The region x coordinate.
	 */
	public int getTopLeftRegionX() {
		return (x / 8) - 6;
	}

	/**
	 * Gets the y coordinate of the region.
	 * @return The region y coordinate.
	 */
	public int getTopLeftRegionY() {
		return (y / 8) - 6;
	}

	/**
	 * Gets the x coordinate of the central region.
	 * @return The x coordinate of the central region.
	 */
	public int getCentralRegionX() {
		return x / 8;
	}

	/**
	 * Gets the y coordinate of the central region.
	 * @return The y coordinate of the central region.
	 */
	public int getCentralRegionY() {
		return y / 8;
	}

	/**
	 * Gets the x coordinate inside the region of this position.
	 * @return The local x coordinate.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the y coordinate inside the region of this position.
	 * @return The local y coordinate.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local x coordinate inside the region of the {@code base}
	 * position.
	 * @param base The base position.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Position base) {
		return x - (base.getTopLeftRegionX() * 8);
	}

	/**
	 * Gets the local y coordinate inside the region of the {@code base}
	 * position.
	 * @param base The base position.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Position base) {
		return y - (base.getTopLeftRegionY() * 8);
	}

	@Override
	public int hashCode() {
		return ((height << 30) & 0xC0000000) | ((y << 15) & 0x3FFF8000) | (x & 0x7FFF);
	}

	/**
	 * Gets the distance between this position and another position. Only X and
	 * Y are considered (i.e. 2 dimensions).
	 * @param other The other position.
	 * @return The distance.
	 */
	public int getDistance(Position other) {
		int deltaX = x - other.x;
		int deltaY = y - other.y;
		// TODO will rounding up interfere with other stuff?
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}

	/**
	 * Gets the longest horizontal or vertical delta between the two positions.
	 * @param other The other position.
	 * @return The longest horizontal or vertical delta.
	 */
	public int getLongestDelta(Position other) {
		int deltaX = x - other.x;
		int deltaY = y - other.y;
		return Math.max(deltaX, deltaY);
	}

	/**
	 * Checks if the position is within distance of another.
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
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Position other = (Position) obj;
		if (height != other.height) {
			return false;
		}
		if (x != other.x) {
			return false;
		}
		if (y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return Position.class.getName() + " [x=" + x + ", y=" + y + ", height=" + height + "]";
	}

}
