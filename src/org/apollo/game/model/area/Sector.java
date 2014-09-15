package org.apollo.game.model.area;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Entity.EntityType;

import com.google.common.collect.ImmutableList;

/**
 * An 8x8 area of the map.
 * 
 * @author Major
 */
public final class Sector {

	/**
	 * The width and length of a sector, in tiles.
	 */
	public static final int SECTOR_SIZE = 8;

	/**
	 * The sector coordinates of this sector.
	 */
	private final SectorCoordinates coordinates;

	/**
	 * A map of positions to entities in that position.
	 */
	private final Map<Position, List<Entity>> entities = new HashMap<>();

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
	 */
	public void addEntity(Entity entity) {
		Position position = entity.getPosition();
		List<Entity> entities = this.entities.get(position);
		if (entities == null) {
			entities = new ArrayList<>();
		}

		entities.add(entity);
		this.entities.put(position, entities);
		notifyListeners(entity, SectorOperation.ADD);
	}

	/**
	 * Checks if this sector contains the specified entity.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if this sector contains the entity, otherwise {@code false}.
	 */
	public boolean contains(Entity entity) {
		Position position = entity.getPosition();
		List<Entity> entities = this.entities.get(position);

		return entities != null && entities.contains(entity);
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
	 * Gets an {@link ImmutableList} containing every {@link Entity} in this sector.
	 * 
	 * @return The list.
	 */
	public List<Entity> getEntities() {
		List<Entity> combined = new ArrayList<>();
		this.entities.values().forEach(entities -> combined.addAll(entities));
		return ImmutableList.copyOf(combined);
	}

	/**
	 * Gets a shallow copy of the {@link List} of {@link Entity} objects at the specified {@link Position}. The returned
	 * type will be {@link ImmutableList}.
	 * 
	 * @param position The position containing the entities.
	 * @return The list.
	 */
	public List<Entity> getEntities(Position position) {
		List<Entity> entities = this.entities.get(position);
		if (entities == null) {
			this.entities.put(position, new ArrayList<>());
			return Collections.emptyList();
		}

		return ImmutableList.copyOf(entities);
	}

	/**
	 * Gets a shallow copy of the {@link List} of {@link Entity}s with the specified {@link EntityType}. The returned
	 * list will be an {@link ImmutableList}. Type will be inferred from the call, so ensure that the entity type and
	 * the reference correspond, or this method will fail at runtime.
	 * 
	 * @param position The {@link Position} containing the entities.
	 * @param type The {@link EntityType}.
	 * @return The list of entities.
	 */
	public <T extends Entity> List<T> getEntities(Position position, EntityType type) {
		List<Entity> entities = this.entities.get(position);
		if (entities == null) {
			this.entities.put(position, new ArrayList<>());
			return ImmutableList.of();
		}

		@SuppressWarnings("unchecked")
		List<T> filtered = (List<T>) entities.stream().filter(e -> e.getEntityType() == type).collect(Collectors.toList());
		return ImmutableList.copyOf(filtered);
	}

	/**
	 * Notifies the {@link SectorListener}s registered to this sector that an update has occurred.
	 * 
	 * @param entity The {@link Entity} that was updated.
	 * @param operation The {@link SectorOperation} that occurred.
	 */
	public void notifyListeners(Entity entity, SectorOperation operation) {
		listeners.forEach(listener -> listener.execute(this, entity, operation));
	}

	/**
	 * Removes a {@link Entity} from this sector.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if the entity was removed, otherwise {@code false}.
	 */
	public boolean removeEntity(Entity entity) {
		Position position = entity.getPosition();
		List<Entity> entities = this.entities.get(position);

		if (entities != null && entities.remove(entity)) {
			notifyListeners(entity, SectorOperation.REMOVE);
			return true;
		}
		return false;
	}

}