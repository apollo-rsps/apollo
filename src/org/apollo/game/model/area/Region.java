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

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;

/**
 * An 8x8 area of the map.
 * 
 * @author Major
 */
public final class Region {

	/**
	 * The width and length of a Region, in tiles.
	 */
	public static final int REGION_SIZE = 8;

	/**
	 * The default size of newly-created sets, to reduce memory usage.
	 */
	private static final int DEFAULT_SET_SIZE = 2;

	/**
	 * The RegionCoordinates of this Region.
	 */
	private final RegionCoordinates coordinates;

	/**
	 * The Map of Positions to Entities in that Position.
	 */
	private final Map<Position, Set<Entity>> entities = new HashMap<>();

	/**
	 * A List of RegionListeners registered to this Region.
	 */
	private final List<RegionListener> listeners = new ArrayList<>();

	/**
	 * The CollisionMatrix.
	 */
	private final CollisionMatrix[] matrices = CollisionMatrix.createMatrices(Position.HEIGHT_LEVELS, REGION_SIZE, REGION_SIZE);

	/**
	 * Creates a new Region.
	 * 
	 * @param x The x coordinate of the Region.
	 * @param y The y coordinate of the Region.
	 */
	public Region(int x, int y) {
		this(new RegionCoordinates(x, y));
	}

	/**
	 * Creates a new Region with the specified {@link RegionCoordinates}.
	 * 
	 * @param coordinates The coordinates.
	 */
	public Region(RegionCoordinates coordinates) {
		this.coordinates = coordinates;
	}

	/**
	 * Adds a {@link Entity} from to Region. Note that this does not spawn the Entity, or do any other action other than
	 * register it to this Region.
	 * 
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region.
	 */
	public void addEntity(Entity entity) {
		Position position = entity.getPosition();
		checkPosition(position);

		Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));
		local.add(entity);

		notifyListeners(entity, RegionOperation.ADD);
	}

	/**
	 * Checks if this Region contains the specified Entity.
	 * <p>
	 * This method operates in constant time.
	 * 
	 * @param entity The Entity.
	 * @return {@code true} if this Region contains the Entity, otherwise {@code false}.
	 */
	public boolean contains(Entity entity) {
		Position position = entity.getPosition();
		Set<Entity> local = entities.get(position);

		return local != null && local.contains(entity);
	}

	/**
	 * Gets this Region's {@link RegionCoordinates}.
	 * 
	 * @return The Region coordinates.
	 */
	public RegionCoordinates getCoordinates() {
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
	 * Both the {@code old} and current positions of the Entity must belong to this Region.
	 * 
	 * @param old The old position of the Entity.
	 * @param entity The Entity to move.
	 * @throws IllegalArgumentException If either of the positions do not belong to this Region.
	 */
	public void moveEntity(Position old, Entity entity) {
		Position position = entity.getPosition();
		checkPosition(old);
		checkPosition(position);

		Set<Entity> local = entities.get(old);

		if (local == null || !local.remove(entity)) {
			throw new IllegalArgumentException("Entity belongs in this Region (" + this + ") but does not exist.");
		}

		local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));

		local.add(entity);
		notifyListeners(entity, RegionOperation.MOVE);
	}

	/**
	 * Notifies the {@link RegionListener}s registered to this Region that an update has occurred.
	 * 
	 * @param entity The {@link Entity} that was updated.
	 * @param operation The {@link RegionOperation} that occurred.
	 */
	public void notifyListeners(Entity entity, RegionOperation operation) {
		listeners.forEach(listener -> listener.execute(this, entity, operation));
	}

	/**
	 * Removes a {@link Entity} from this Region.
	 * 
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region, or if it was never added.
	 */
	public void removeEntity(Entity entity) {
		Position position = entity.getPosition();
		checkPosition(position);

		Set<Entity> local = entities.get(position);

		if (local == null || !local.remove(entity)) {
			throw new IllegalArgumentException("Entity belongs in this Region (" + this + ") but does not exist.");
		}

		notifyListeners(entity, RegionOperation.REMOVE);
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
		int x = position.getX(), y = position.getY();

		return !matrix.untraversable(x % REGION_SIZE, y % REGION_SIZE, entity, direction);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("coordinates", coordinates).toString();
	}

	/**
	 * Checks that the specified {@link Position} is included in this Region.
	 * 
	 * @param position The position.
	 * @throws IllegalArgumentException If the specified position is not included in this Region.
	 */
	private void checkPosition(Position position) {
		Preconditions.checkArgument(coordinates.equals(RegionCoordinates.fromPosition(position)),
				"Position is not included in this Region.");
	}

}