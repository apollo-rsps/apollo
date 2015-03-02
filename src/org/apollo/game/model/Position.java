package org.apollo.game.model;

import org.apollo.game.model.area.Sector;
import org.apollo.game.model.area.SectorCoordinates;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

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
		Preconditions.checkArgument(height >= 0 && height < HEIGHT_LEVELS, "Height level out of bounds.");

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
	 * Gets the x coordinate of the central sector.
	 * 
	 * @return The x coordinate of the central sector.
	 */
	public int getCentralSectorX() {
		return getX() / 8;
	}

	/**
	 * Gets the y coordinate of the central sector.
	 * 
	 * @return The y coordinate of the central sector.
	 */
	public int getCentralSectorY() {
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
		return packed >> 30;
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
		return getX() - base.getTopLeftSectorX() * 8;
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
		return getY() - base.getTopLeftSectorY() * 8;
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
	 * Returns the {@link SectorCoordinates} of the {@link Sector} this position is inside.
	 * 
	 * @return The sector coordinates.
	 */
	public SectorCoordinates getSectorCoordinates() {
		return SectorCoordinates.fromPosition(this);
	}

	/**
	 * Gets the x coordinate of the sector this position is in.
	 * 
	 * @return The sector x coordinate.
	 */
	public int getTopLeftSectorX() {
		return getX() / 8 - 6;
	}

	/**
	 * Gets the y coordinate of the sector this position is in.
	 * 
	 * @return The sector y coordinate.
	 */
	public int getTopLeftSectorY() {
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
		return (packed >> 15) & 0x7FFF;
	}

	@Override
	public int hashCode() {
		return packed;
	}

	/**
	 * Returns whether or not this position is inside the specified {@link Sector}.
	 * 
	 * @param sector The sector.
	 * @return {@code true} if this position is inside the specified sector, otherwise {@code false}.
	 */
	public boolean inside(Sector sector) {
		SectorCoordinates coordinates = sector.getCoordinates();
		return coordinates.equals(getSectorCoordinates());
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
		return deltaX <= distance && deltaY <= distance;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("x", getX()).add("y", getY()).add("height", getHeight()).add("sector", getSectorCoordinates()).toString();
	}

}