package org.apollo.game.model.area;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionMatrix;
import org.apollo.game.model.area.update.GroupableEntity;
import org.apollo.game.model.area.update.UpdateOperation;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.obj.DynamicGameObject;
import org.apollo.game.model.entity.obj.StaticGameObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An 8x8 area of the map.
 *
 * @author Major
 */
public final class Region {

	/**
	 * A {@link RegionListener} for {@link UpdateOperation}s.
	 */
	private static final class UpdateRegionListener implements RegionListener {

		@Override
		public void execute(Region region, Entity entity, EntityUpdateType update) {
			EntityType type = entity.getEntityType();
			if (!type.isMob()) {
				region.record((Entity & GroupableEntity) entity, update);
			}
		}

	}

	/**
	 * The message of the exception thrown when a CollisionMatrix with an illegal height is requested.
	 */
	private static final String ILLEGAL_MATRIX_HEIGHT = "Matrix height level must be [0, %d), received %d.";

	/**
	 * The width and length of a Region, in tiles.
	 */
	public static final int SIZE = 8;

	/**
	 * The radius of viewable regions.
	 */
	public static final int VIEWABLE_REGION_RADIUS = 3;

	/**
	 * The width of the viewport of every Player, in tiles.
	 */
	public static final int VIEWPORT_WIDTH = SIZE * 13;

	/**
	 * The default size of newly-created Lists, to reduce memory usage.
	 */
	private static final int DEFAULT_LIST_SIZE = 2;

	/**
	 * The RegionCoordinates of this Region.
	 */
	private final RegionCoordinates coordinates;

	/**
	 * The Map of Positions to Entities in that Position.
	 */
	private final Map<Position, Set<Entity>> entities = new ConcurrentHashMap<>();

	/**
	 * A List of RegionListeners registered to this Region.
	 */
	private final List<RegionListener> listeners = new ArrayList<>();

	/**
	 * The CollisionMatrix.
	 */
	private final CollisionMatrix[] matrices = CollisionMatrix.createMatrices(Position.HEIGHT_LEVELS, SIZE, SIZE);

	/**
	 * The List of Sets containing RegionUpdateMessages that specifically remove StaticGameObjects. The
	 * List is ordered based on the height level the RegionUpdateMessages concern.
	 */
	private final List<Set<RegionUpdateMessage>> removedObjects = new ArrayList<>(Position.HEIGHT_LEVELS);

	/**
	 * The List of Sets containing RegionUpdateMessages. The List is ordered based on the height level the
	 * RegionUpdateMessages concern. This only contains the updates to this Region that have occurred in the last
	 * pulse.
	 */
	private final List<Set<RegionUpdateMessage>> updates = new ArrayList<>(Position.HEIGHT_LEVELS);

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
	 * @param coordinates The RegionCoordinates.
	 */
	public Region(RegionCoordinates coordinates) {
		this.coordinates = coordinates;
		listeners.add(new UpdateRegionListener());

		for (int height = 0; height < Position.HEIGHT_LEVELS; height++) {
			removedObjects.add(new HashSet<>());
			updates.add(new HashSet<>(DEFAULT_LIST_SIZE));
		}
	}

	/**
	 * Adds a {@link Entity} to the Region. Note that this does not spawn the Entity, or do any other action other than
	 * register it to this Region.
	 *
	 * @param entity The Entity.
	 * @param notify Whether or not the {@link RegionListener}s for this Region should be notified.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region.
	 */
	public void addEntity(Entity entity, boolean notify) {
		EntityType type = entity.getEntityType();
		Position position = entity.getPosition();
		checkPosition(position);

		if (!type.isTransient()) {
			Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_LIST_SIZE));
			local.add(entity);
		}

		if (notify) {
			notifyListeners(entity, EntityUpdateType.ADD);
		}
	}

	/**
	 * Adds a {@link Entity} to the Region. Note that this does not spawn the Entity, or do any other action other than
	 * register it to this Region.
	 *
	 * By default, this method notifies RegionListeners for this region of the addition.
	 *
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region.
	 */
	public void addEntity(Entity entity) {
		addEntity(entity, true);
	}

	public void addListener(RegionListener listener) {
		listeners.add(listener);
	}

	/**
	 * Checks if this Region contains the specified Entity.
	 *
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
	 * Returns whether or not the specified {@link Position} is inside this Region.
	 *
	 * @param position The Position.
	 * @return {@code true} iff the specified Position is inside this Region.
	 */
	public boolean contains(Position position) {
		return coordinates.equals(position.getRegionCoordinates());
	}

	/**
	 * Encodes the contents of this Region into a {@link Set} of {@link RegionUpdateMessage}s, to be sent to a client.
	 *
	 * @return The Set of RegionUpdateMessages.
	 */
	public Set<RegionUpdateMessage> encode(int height) {
		Set<RegionUpdateMessage> additions = entities.values().stream()
			.flatMap(Set::stream) // TODO: Stop hurting my eyeballs.
			.filter(entity -> !entity.getEntityType().isMob() && !(entity instanceof StaticGameObject) && (!(entity instanceof DynamicGameObject) || entity.getPosition().getHeight() == height))
			.map(entity -> ((GroupableEntity) entity).toUpdateOperation(this, EntityUpdateType.ADD).toMessage())
			.collect(Collectors.toSet());

		ImmutableSet.Builder<RegionUpdateMessage> builder = ImmutableSet.builder();
		builder.addAll(additions).addAll(updates.get(height)).addAll(removedObjects.get(height));
		return builder.build();
	}

	/**
	 * Gets this Region's {@link RegionCoordinates}.
	 *
	 * @return The RegionCoordinates.
	 */
	public RegionCoordinates getCoordinates() {
		return coordinates;
	}

	/**
	 * Gets an intermediate {@link Stream} from the {@link Set} of
	 * {@link Entity}s with the specified {@link EntityType} (s). Type will be
	 * inferred from the call, so ensure that the Entity type and the reference
	 * correspond, or this method will fail at runtime.
	 *
	 * @param types The {@link EntityType}s.
	 * @return The Stream of Entity objects.
	 */
	@SuppressWarnings("unchecked")
	public <T extends Entity> Stream<T> getEntities(EntityType... types) {
		Set<EntityType> set = ImmutableSet.copyOf(types);
		return (Stream<T>) entities.values().stream().flatMap(Collection::stream)
			.filter(entity -> set.contains(entity.getEntityType()));
	}

	/**
	 * Gets a shallow copy of the {@link Set} of {@link Entity} objects at the specified {@link Position}. The returned
	 * type will be immutable.
	 *
	 * @param position The Position containing the entities.
	 * @return The Set. Will be immutable.
	 */
	public Set<Entity> getEntities(Position position) {
		Set<Entity> set = entities.get(position);
		return (set == null) ? ImmutableSet.of() : ImmutableSet.copyOf(set);
	}

	/**
	 * Gets a shallow copy of the {@link Set} of {@link Entity}s with the specified {@link EntityType}(s). The returned
	 * type will be immutable. Type will be inferred from the call, so ensure that the Entity type and the reference
	 * correspond, or this method will fail at runtime.
	 *
	 * @param position The {@link Position} containing the entities.
	 * @param types The {@link EntityType}s.
	 * @return The Set of Entity objects.
	 */
	public <T extends Entity> Set<T> getEntities(Position position, EntityType... types) {
		Set<Entity> local = entities.get(position);
		if (local == null) {
			return ImmutableSet.of();
		}

		Set<EntityType> set = ImmutableSet.copyOf(types);
		@SuppressWarnings("unchecked")
		Set<T> filtered = (Set<T>) local.stream().filter(entity -> set.contains(entity.getEntityType()))
			.collect(Collectors.toSet());
		return ImmutableSet.copyOf(filtered);
	}

	/**
	 * Gets the position offset for the specified {@link Entity}.
	 *
	 * @param entity The Entity.
	 * @return The position offset.
	 */
	public int getPositionOffset(Entity entity) {
		return getPositionOffset(entity.getPosition());
	}

	/**
	 * Gets the position offset for the specified {@link Position}.
	 *
	 * @param position The Entity.
	 * @return The position offset.
	 */
	public int getPositionOffset(Position position) {
		RegionCoordinates coordinates = getCoordinates();
		int dx = position.getX() - coordinates.getAbsoluteX();
		int dy = position.getY() - coordinates.getAbsoluteY();

		Preconditions.checkArgument(dx >= 0 && dx < Region.SIZE, position + " not in expected Region of " + toString() + ".");
		Preconditions.checkArgument(dy >= 0 && dy < Region.SIZE, position + " not in expected Region of " + toString() + ".");

		return dx << 4 | dy;
	}

	/**
	 * Gets the {@link Set} of {@link RegionCoordinates} of Regions that are viewable from the specified
	 * {@link Position}.
	 *
	 * @return The Set of RegionCoordinates.
	 */
	public Set<RegionCoordinates> getSurrounding() {
		int localX = coordinates.getX(), localY = coordinates.getY();
		int maxX = localX + VIEWABLE_REGION_RADIUS, maxY = localY + VIEWABLE_REGION_RADIUS;

		Set<RegionCoordinates> viewable = new HashSet<>();
		for (int x = localX - VIEWABLE_REGION_RADIUS; x < maxX; x++) {
			for (int y = localY - VIEWABLE_REGION_RADIUS; y < maxY; y++) {
				viewable.add(new RegionCoordinates(x, y));
			}
		}

		return viewable;
	}

	/**
	 * Gets the {@link CollisionMatrix} at the specified height level.
	 *
	 * @param height The height level.
	 * @return The CollisionMatrix.
	 */
	public CollisionMatrix getMatrix(int height) {
		Preconditions.checkElementIndex(height, matrices.length, String.format(ILLEGAL_MATRIX_HEIGHT, matrices.length, height));
		return matrices[height];
	}

	/**
	 * Gets all of the {@link CollisionMatrix} objects in this {@code Region}.
	 *
	 * @return The collision matrices of this region.
	 */
	public CollisionMatrix[] getMatrices() {
		return matrices;
	}

	/**
	 * Gets the {@link Set} of {@link RegionUpdateMessage}s that have occurred in the last pulse. This method can
	 * only be called <strong>once</strong> per pulse.
	 *
	 * @param height The height level to get the RegionUpdateMessages for.
	 * @return The Set of RegionUpdateMessages.
	 */
	public Set<RegionUpdateMessage> getUpdates(int height) {
		Set<RegionUpdateMessage> updates = this.updates.get(height);
		Set<RegionUpdateMessage> copy = ImmutableSet.copyOf(updates);

		updates.clear();
		return copy;
	}

	/**
	 * Notifies the {@link RegionListener}s registered to this Region that an update has occurred.
	 *
	 * @param entity The {@link Entity} that was updated.
	 * @param type The {@link EntityUpdateType} that occurred.
	 */
	public void notifyListeners(Entity entity, EntityUpdateType type) {
		listeners.forEach(listener -> listener.execute(this, entity, type));
	}

	/**
	 * Removes an {@link Entity} from this Region.
	 *
	 * @param entity The Entity.
	 * @throws IllegalArgumentException If the Entity does not belong in this Region, or if it was never added.
	 */
	public void removeEntity(Entity entity) {
		EntityType type = entity.getEntityType();
		if (type.isTransient()) {
			throw new IllegalArgumentException("Tried to remove a transient Entity (" + entity + ") from " +
				"(" + this + ").");
		}

		Position position = entity.getPosition();
		checkPosition(position);

		Set<Entity> local = entities.get(position);

		if (local == null || !local.remove(entity)) {
			throw new IllegalArgumentException("Entity (" + entity + ") belongs in (" + this + ") but does not exist.");
		}

		notifyListeners(entity, EntityUpdateType.REMOVE);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("coordinates", coordinates).toString();
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

		return !matrix.untraversable(x % SIZE, y % SIZE, entity, direction);
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

	/**
	 * Records the specified {@link GroupableEntity} as being updated this pulse.
	 *
	 * @param entity The GroupableEntity.
	 * @param update The {@link EntityUpdateType}.
	 * @throws UnsupportedOperationException If the specified Entity cannot be operated on in this manner.
	 */
	private <T extends Entity & GroupableEntity> void record(T entity, EntityUpdateType update) {
		UpdateOperation<?> operation = entity.toUpdateOperation(this, update);
		RegionUpdateMessage message = operation.toMessage(), inverse = operation.inverse();

		int height = entity.getPosition().getHeight();
		Set<RegionUpdateMessage> updates = this.updates.get(height);

		EntityType type = entity.getEntityType();

		if (type == EntityType.STATIC_OBJECT) { // TODO set/clear collision matrix values
			if (update == EntityUpdateType.REMOVE) {
				removedObjects.get(height).add(message);
			} else { // TODO should this really be possible?
				removedObjects.get(height).remove(inverse);
			}
		}

		if (update == EntityUpdateType.REMOVE && !type.isTransient()) {
			updates.remove(inverse);
		}

		updates.add(message);
	}

}