package org.apollo.game.model.sector;

/**
 * An immutable class representing the coordinates of a sector.
 * 
 * @author Graham
 */
public final class SectorCoordinates {

	/**
	 * The x coordinate.
	 */
	private final int x;

	/**
	 * The y coordinate.
	 */
	private final int y;

	/**
	 * Creates the sector coordinates.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public SectorCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final SectorCoordinates other = (SectorCoordinates) obj;
		if (x != other.x || y != other.y) {
			return false;
		}
		return true;
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
		int hash = 7;
		hash = 61 * hash + x;
		hash = 61 * hash + y;
		return hash;
	}

}