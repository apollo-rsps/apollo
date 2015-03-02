package org.apollo.game.model.area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionMatrix;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Entity.EntityType;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * An 8x8 area of the map.
 * 
 * @author Major
 */
public final class Sector {

	/**
	 * The width and length of a Sector, in tiles.
	 */
	public static final int SECTOR_SIZE = 8;

	/**
	 * The default size of newly-created sets, to reduce memory usage.
	 */
	private static final int DEFAULT_SET_SIZE = 2;

	/**
	 * The SectorCoordinates of this Sector.
	 */
	private final SectorCoordinates coordinates;

	/**
	 * The Map of Positions to Entities in that Position.
	 */
	private final Map<Position, Set<Entity>> entities = new HashMap<>();

	/**
	 * A List of SectorListeners registered to this Sector.
	 */
	private final List<SectorListener> listeners = new ArrayList<>();

	/**
	 * The CollisionMatrix.
	 */
	private final CollisionMatrix[] matrices = CollisionMatrix.createMatrices(Position.HEIGHT_LEVELS, SECTOR_SIZE, SECTOR_SIZE);

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
	 * Adds a {@link Entity} from to sector. Note that this does not spawn the Entity, or do any other action other than
	 * register it to this sector.
	 * 
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this sector.
	 */
	public void addEntity(Entity entity) {
		Position position = entity.getPosition();
		checkPosition(position);

		Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));
		local.add(entity);

		notifyListeners(entity, SectorOperation.ADD);
	}

	/**
	 * Checks if this sector contains the specified Entity.
	 * <p>
	 * This method operates in constant time.
	 * 
	 * @param entity The Entity.
	 * @return {@code true} if this sector contains the Entity, otherwise {@code false}.
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
	 * Gets a shallow copy of the {@link Set} of {@link Entity} objects at the specified {@link Position}. The returned
	 * type will be immutable.
	 * 
	 * @param position The position containing the entities.
	 * @return The list.
	 */
	public Set<Entity> getEntities(Position position) {
		Set<Entity> set = entities.get(position);
		return (set == null) ? ImmutableSet.of() : ImmutableSet.copyOf(set);
	}

	/**
	 * Gets a shallow copy of the {@link Set} of {@link Entity}s with the specified {@link EntityType}. The returned
	 * type will be immutable. Type will be inferred from the call, so ensure that the Entity type and the reference
	 * correspond, or this method will fail at runtime.
	 * 
	 * @param position The {@link Position} containing the entities.
	 * @param type The {@link EntityType}.
	 * @return The set of entities.
	 */
	public <T extends Entity> Set<T> getEntities(Position position, EntityType type) {
		Set<Entity> local = entities.get(position);
		if (local == null) {
			return ImmutableSet.of();
		}

		@SuppressWarnings("unchecked")
		Set<T> filtered = (Set<T>) local.stream().filter(Entity -> Entity.getEntityType() == type).collect(Collectors.toSet());
		return ImmutableSet.copyOf(filtered);
	}

	/**
	 * Gets the {@link CollisionMatrix} at the specified height level.
	 * 
	 * @param height The height level.
	 * @return The CollisionMatrix.
	 */
	public CollisionMatrix getMatrix(int height) {
		Preconditions.checkElementIndex(height, matrices.length, "Matrix height level must be [0, " + matrices.length + ").");
		return matrices[height];
	}

	/**
	 * Moves the {@link Entity} that was in the specified {@code old} {@link Position}, to the current position of the
	 * Entity.
	 * <p>
	 * Both the {@code old} and current positions of the Entity must belong to this sector.
	 * 
	 * @param old The old position of the Entity.
	 * @param entity The Entity to move.
	 * @throws IllegalArgumentException If either of the positions do not belong to this sector.
	 */
	public void moveEntity(Position old, Entity entity) {
		Position position = entity.getPosition();
		checkPosition(old);
		checkPosition(position);

		Set<Entity> local = entities.get(old);

		if (local == null || !local.remove(entity)) {
			throw new IllegalArgumentException("Entity belongs in this sector but does not exist.");
		}

		local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));

		local.add(entity);
		notifyListeners(entity, SectorOperation.MOVE);

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
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this sector, or if it was never added.
	 */
	public void removeEntity(Entity entity) {
		Position position = entity.getPosition();
		checkPosition(position);

		Set<Entity> local = entities.get(position);

		if (local == null || !local.remove(entity)) {
			throw new IllegalArgumentException("Entity belongs in this sector but does not exist.");
		}

		notifyListeners(entity, SectorOperation.REMOVE);
	}

	/**
	 * Returns whether or not an Entity of the specified {@link EntityType type} can traverse the tile at the specified
	 * coordinate pair.
	 * 
	 * @param position The {@link Position} of the tile.
	 * @param entity The {@link EntityType}.
	 * @param direction The {@link Direction} the Entity is approaching from.
	 * @return {@code true} if the tile at the specified coordinate pair is traversable, {@code false} if not.
	 */
	public boolean traversable(Position position, EntityType entity, Direction direction) {
		CollisionMatrix matrix = matrices[position.getHeight()];
		int x = position.getLocalX(), y = position.getLocalY();

		return matrix.traversable(x, y, entity, direction);
	}

	/**
	 * Checks that the specified {@link Position} is included in this sector.
	 * 
	 * @param position The position.
	 * @throws IllegalArgumentException If the specified position is not included in this sector.
	 */
	private void checkPosition(Position position) {
		Preconditions.checkArgument(coordinates.equals(SectorCoordinates.fromPosition(position)), "Position is not included in this sector.");
	}

}