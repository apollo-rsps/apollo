package org.apollo.game.model.region;

/**
 * An immutable class which contains the coordinates of a region.
 * @author Graham
 */
public final class RegionCoordinates {

	/**
	 * The X coordinate.
	 */
	private final int x;

	/**
	 * The Y coordinate.
	 */
	private final int y;

	/**
	 * Creates the region coordinates.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public RegionCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the X coordinate.
	 * @return The X coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the Y coordinate.
	 * @return The Y coordinate.
	 */
	public int getY() {
		return y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final RegionCoordinates other = (RegionCoordinates) obj;
		if (this.x != other.x) {
			return false;
		}
		if (this.y != other.y) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 61 * hash + this.x;
		hash = 61 * hash + this.y;
		return hash;
	}

}
