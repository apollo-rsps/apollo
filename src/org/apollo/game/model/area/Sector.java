package org.apollo.game.model.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Entity.EntityType;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * An 8x8 area of the map.
 * 
 * @author Major
 */
public final class Sector {

	/**
	 * The default size of newly-created sets, to reduce memory usage.
	 */
	private static final int DEFAULT_SET_SIZE = 2;

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
	private final Map<Position, Set<Entity>> entities = new HashMap<>();

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
		checkPosition(position);
		Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));

		local.add(entity);
		notifyListeners(entity, SectorOperation.ADD);
	}

	/**
	 * Checks that the specified {@link Position} is included in this sector.
	 * 
	 * @param position The position.
	 * @throws IllegalArgumentException If the specified position is not included in this sector.
	 */
	private void checkPosition(Position position) {
		Preconditions.checkArgument(coordinates.equals(SectorCoordinates.fromPosition(position)),
				"Position is not included in this sector.");
	}

	/**
	 * Checks if this sector contains the specified entity.
	 * <p>
	 * This method operates in constant time.
	 * 
	 * @param entity The entity.
	 * @return {@code true} if this sector contains the entity, otherwise {@code false}.
	 */
	public boolean contains(Entity entity) {
		Position position = entity.getPosition();
		Set<Entity> local = entities.get(position);

		return local != null && local.contains(entity);
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
	 * Gets a shallow copy of the {@link List} of {@link Entity} objects at the specified {@link Position}. The returned
	 * type will be {@link ImmutableList}.
	 * 
	 * @param position The position containing the entities.
	 * @return The list.
	 */
	public List<Entity> getEntities(Position position) {
		return ImmutableList.copyOf(entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE)));
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
	public <T extends Entity> Set<T> getEntities(Position position, EntityType type) {
		Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));

		@SuppressWarnings("unchecked")
		Set<T> filtered = (Set<T>) local.stream().filter(entity -> entity.getEntityType() == type).collect(Collectors.toSet());
		return ImmutableSet.copyOf(filtered);
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
	 * @throws IllegalArgumentException If the entity does not belong in this sector, or if it was never added.
	 */
	public void removeEntity(Entity entity) {
		try {
			Position position = entity.getPosition();
			checkPosition(position);

			Set<Entity> local = entities.get(position);

			if (local == null || !local.remove(entity)) {
				throw new IllegalArgumentException("Entity belongs in this sector but does not exist.");
			}

			notifyListeners(entity, SectorOperation.REMOVE);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

}