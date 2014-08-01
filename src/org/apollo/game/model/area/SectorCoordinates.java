package org.apollo.game.model.area;

import org.apollo.game.model.Position;

/**
 * An immutable class representing the coordinates of a sector, where the coordinates ({@code x, y}) are the top-left of
 * the sector.
 * 
 * @author Graham
 */
public final class SectorCoordinates {

	/**
	 * Gets a pair of sector coordinates from a {@link Position}.
	 * 
	 * @param position The position.
	 * @return The sector coordinates.
	 */
	public static SectorCoordinates fromPosition(Position position) {
		return new SectorCoordinates(position.getTopLeftSectorX(), position.getTopLeftSectorY());
	}

	/**
	 * The x coordinate of this sector.
	 */
	private final int x;

	/**
	 * The y coordinate of this sector.
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
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		final SectorCoordinates other = (SectorCoordinates) obj;
		return x == other.x && y == other.y;
	}

	/**
	 * Gets the x coordinate (equivalent to the {@link Position#getTopLeftSectorX()} of a position within this sector).
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return x;
	}

	/**
	 * Gets the y coordinate (equivalent to the {@link Position#getTopLeftSectorY()} of a position within this sector).
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

}