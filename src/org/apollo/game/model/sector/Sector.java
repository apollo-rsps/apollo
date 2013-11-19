package org.apollo.game.model.sector;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.Entity;

/**
 * Represents an 8x8 area of the map.
 * 
 * @author Major
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
	 * A {@link List} of every {@link Entity} in this sector.
	 */
	private final List<Entity> entities = new ArrayList<Entity>();

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
	 * Adds a {@link Entity} from to sector. Note that this does not spawn the entity, or do any other action
	 * other than register it to this sector.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if the entity was added, otherwise {@code false}.
	 */
	public boolean addEntity(Entity entity) {
		return entities.add(entity);
	}

	/**
	 * Gets this sector's {@link SectorCoordinates}.
	 * 
	 * @return The sector coordinates.
	 */
	public SectorCoordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Removes a {@link Entity} from this sector.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if the entity was removed, otherwise {@code false}.
	 */
	public boolean removeEntity(Entity entity) {
		return entities.remove(entity);
	}

}