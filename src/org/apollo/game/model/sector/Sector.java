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
	 * The width and height of a sector, in tiles.
	 */
	public static final int SECTOR_SIZE = 8;

	/**
	 * The sector coordinates of this sector.
	 */
	private final SectorCoordinates coordinates;

	/**
	 * A list of every entity in this sector.
	 */
	private final List<Entity> entities = new ArrayList<>();

	/**
	 * A list of listeners registered to this sector.
	 */
	private final List<SectorListener> listeners = new ArrayList<>();

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
	 * Adds a {@link Entity} from to sector. Note that this does not spawn the entity, or do any other action other than
	 * register it to this sector.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if the entity was added, otherwise {@code false}.
	 */
	public boolean addEntity(Entity entity) {
		if (entities.add(entity)) {
			notifyListeners(entity);
			return true;
		}
		return false;
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
	 * Notifies the listeners registered to this sector that an update has occurred.
	 * 
	 * @param entity The entity that was updated.
	 */
	public void notifyListeners(Entity entity) {
		for (SectorListener listener : listeners) {
			listener.execute(this, entity);
		}
	}

	/**
	 * Removes a {@link Entity} from this sector.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if the entity was removed, otherwise {@code false}.
	 */
	public boolean removeEntity(Entity entity) {
		if (entities.remove(entity)) {
			notifyListeners(entity);
			return true;
		}
		return false;
	}

}