package org.apollo.game.model.sector;

/**
 * Represents an 8x8 area of the map.
 * 
 * @author Graham
 */
public final class Sector {

	/**
	 * The width and height of the sector, in tiles.
	 */
	public static final int SECTOR_SIZE = 8;

	/**
	 * The {@link SectorCoordinates} of this sector.
	 */
	private final SectorCoordinates coordinates;

	/**
	 * Creates a new sector.
	 * 
	 * @param x The x coordinate of the sector.
	 * @param y The y coordinate of the sector.
	 */
	public Sector(int x, int y) {
		this(new SectorCoordinates(x, y));
	}

	/**
	 * Creates a new sector with the specified {@link SectorCoordinates}.
	 * 
	 * @param coordinates The coordinates.
	 */
	public Sector(SectorCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Gets the x coordinate of this sector.
	 * 
	 * @return The x coordinate.
	 */
	public int getX() {
		return coordinates.getX();
	}

	/**
	 * Gets the y coordinate of this sector.
	 * 
	 * @return The y coordinate.
	 */
	public int getY() {
		return coordinates.getY();
	}

}