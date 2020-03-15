package org.apollo.game.model.area;

import com.google.common.base.MoreObjects;
import org.apollo.game.model.Position;

/**
 * An immutable class representing the coordinates of a region, where the coordinates ({@code x, y}) are the top-left of
 * the region.
 *
 * @author Graham
 * @author Major
 */
public final class RegionCoordinates {

	/**
	 * Gets the RegionCoordinates for the specified {@link Position}.
	 *
	 * @param position The Position.
	 * @return The RegionCoordinates.
	 */
	public static RegionCoordinates fromPosition(Position position) {
		return new RegionCoordinates(position.getTopLeftRegionX(), position.getTopLeftRegionY());
	}

	/**
	 * The x coordinate of this region.
	 */
	private final int x;

	/**
	 * The y coordinate of this region.
	 */
	private final int y;

	/**
	 * Creates the RegionCoordinates.
	 *
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public RegionCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RegionCoordinates) {
			RegionCoordinates other = (RegionCoordinates) obj;
			return x == other.x && y == other.y;
		}

		return false;
	}

	/**
	 * Gets the absolute x coordinate of this Region (which can be compared directly against {@link Position#getX()}.
	 *
	 * @return The absolute x coordinate.
	 */
	public int getAbsoluteX() {
		return Region.SIZE * (x + 6);
	}

	/**
	 * Gets the absolute y coordinate of this Region (which can be compared directly against {@link Position#getY()}.
	 *
	 * @return The absolute y coordinate.
	 */
	public int getAbsoluteY() {
		return Region.SIZE * (y + 6);
	}

	/**
	 * Gets the x coordinate (equivalent to the {@link Position#getTopLeftRegionX()} of a position within this region).
	 *
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate (equivalent to the {@link Position#getTopLeftRegionY()} of a position within this region).
	 *
	 * @return The y coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x << 16 | y;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("x", x).add("y", y).toString();
	}

}