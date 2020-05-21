package org.apollo.game.model;

import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionCoordinates;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Represents a position in the world.
 *
 * @author Graham
 */
public final class Position {

	/**
	 * The number of height levels, (0, 3] inclusive.
	 */
	public static final int HEIGHT_LEVELS = 4;

	/**
	 * The maximum distance players/NPCs can 'see'.
	 */
	public static final int MAX_DISTANCE = 15;

	/**
	 * The packed integer containing the {@code height, x}, and {@code y} variables.
	 */
	private final int packed;

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
		Preconditions.checkElementIndex(height, HEIGHT_LEVELS, "Height must be [0, 3), received " + height + ".");

		packed = height << 30 | (y & 0x7FFF) << 15 | x & 0x7FFF;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Position) {
			Position other = (Position) obj;
			return packed == other.packed;
		}

		return false;
	}

	/**
	 * Gets the x coordinate of the central region.
	 *
	 * @return The x coordinate of the central region.
	 */
	public int getCentralRegionX() {
		return getX() / 8;
	}

	/**
	 * Gets the y coordinate of the central region.
	 *
	 * @return The y coordinate of the central region.
	 */
	public int getCentralRegionY() {
		return getY() / 8;
	}

	/**
	 * Gets the distance between this position and another position. Only x and y are considered (i.e. 2 dimensions).
	 *
	 * @param other The other position.
	 * @return The distance.
	 */
	public int getDistance(Position other) {
		int deltaX = getX() - other.getX();
		int deltaY = getY() - other.getY();
		return (int) Math.ceil(Math.sqrt(deltaX * deltaX + deltaY * deltaY));
	}

	/**
	 * Gets the height level.
	 *
	 * @return The height level.
	 */
	public int getHeight() {
		return packed >>> 30;
	}

	/**
	 * Gets the x coordinate inside the region of this position.
	 *
	 * @return The local x coordinate.
	 */
	public int getLocalX() {
		return getLocalX(this);
	}

	/**
	 * Gets the local x coordinate inside the region of the {@code base} position.
	 *
	 * @param base The base position.
	 * @return The local x coordinate.
	 */
	public int getLocalX(Position base) {
		return getX() - base.getTopLeftRegionX() * 8;
	}

	/**
	 * Gets the y coordinate inside the region of this position.
	 *
	 * @return The local y coordinate.
	 */
	public int getLocalY() {
		return getLocalY(this);
	}

	/**
	 * Gets the local y coordinate inside the region of the {@code base} position.
	 *
	 * @param base The base position.
	 * @return The local y coordinate.
	 */
	public int getLocalY(Position base) {
		return getY() - base.getTopLeftRegionY() * 8;
	}

	/**
	 * Gets the longest horizontal or vertical delta between the two positions.
	 *
	 * @param other The other position.
	 * @return The longest horizontal or vertical delta.
	 */
	public int getLongestDelta(Position other) {
		int deltaX = Math.abs(getX() - other.getX());
		int deltaY = Math.abs(getY() - other.getY());
		return Math.max(deltaX, deltaY);
	}

	/**
	 * Returns the {@link RegionCoordinates} of the {@link Region} this position is inside.
	 *
	 * @return The region coordinates.
	 */
	public RegionCoordinates getRegionCoordinates() {
		return RegionCoordinates.fromPosition(this);
	}

	/**
	 * Gets the x coordinate of the region this position is in.
	 *
	 * @return The region x coordinate.
	 */
	public int getTopLeftRegionX() {
		return getX() / 8 - 6;
	}

	/**
	 * Gets the y coordinate of the region this position is in.
	 *
	 * @return The region y coordinate.
	 */
	public int getTopLeftRegionY() {
		return getY() / 8 - 6;
	}

	/**
	 * Gets the x coordinate.
	 *
	 * @return The x coordinate.
	 */
	public int getX() {
		return packed & 0x7FFF;
	}

	/**
	 * Gets the y coordinate.
	 *
	 * @return The y coordinate.
	 */
	public int getY() {
		return packed >> 15 & 0x7FFF;
	}

	@Override
	public int hashCode() {
		return packed;
	}

	/**
	 * Returns whether or not this position is inside the specified {@link Region}.
	 *
	 * @param region The region.
	 * @return {@code true} if this position is inside the specified region, otherwise {@code false}.
	 */
	public boolean inside(Region region) {
		RegionCoordinates coordinates = region.getCoordinates();
		return coordinates.equals(getRegionCoordinates());
	}

	/**
	 * Checks if the position is within distance of another.
	 *
	 * @param other The other position.
	 * @param distance The distance.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isWithinDistance(Position other, int distance) {
		int deltaX = Math.abs(getX() - other.getX());
		int deltaY = Math.abs(getY() - other.getY());
		return deltaX <= distance && deltaY <= distance && getHeight() == other.getHeight();
	}

	/**
	 * Creates a new position {@code num} steps from this position in the given direction.
	 *
	 * @param num The number of steps to make.
	 * @param direction The direction to make steps in.
	 * @return A new {@code Position} that is {@code num} steps in {@code direction} ahead of this one.
	 */
	public Position step(int num, Direction direction) {
		return new Position(getX() + (num * direction.deltaX()), getY() + (num * direction.deltaY()), getHeight());
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("x", getX()).add("y", getY()).add("height", getHeight()).add("map", getRegionCoordinates()).toString();
	}
}