package org.apollo.game.model.area;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionMatrix;
import org.apollo.game.model.area.update.UpdateOperation;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.Entity.EntityType;
import org.apollo.game.model.entity.obj.GameObject;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * An 8x8 area of the map.
 *
 * @author Major
 */
public final class Region {

    /**
     * A {@link RegionListener} for {@link UpdateOperation}s.
     *
     * @author Major
     */
    private static final class UpdateRegionListener implements RegionListener {

        @Override
        public void execute(Region region, Entity entity, EntityUpdateType update) {
            EntityType type = entity.getEntityType();
            if (type != EntityType.PLAYER && type != EntityType.NPC) {
                region.record(entity, update);
            }
        }

    }

    /**
     * The width and length of a Region, in tiles.
     */
    public static final int SIZE = 8;

    static final long start = System.currentTimeMillis();

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
    private final CollisionMatrix[] matrices = CollisionMatrix.createMatrices(Position.HEIGHT_LEVELS, SIZE, SIZE);

    /**
     * The Set containing RegionUpdateMessages which can be sent to add every non-Mob Entity in this Region.
     */
    private final List<Map<Entity, RegionUpdateMessage>> snapshots = new ArrayList<>(Position.HEIGHT_LEVELS);

    /**
     * The Set containing UpdateOperations.
     */
    private final List<List<RegionUpdateMessage>> updates = new ArrayList<>(Position.HEIGHT_LEVELS);

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
        listeners.add(new UpdateRegionListener());

        for (int height = 0; height < Position.HEIGHT_LEVELS; height++) {
            snapshots.add(new HashMap<>());
            updates.add(new ArrayList<>(DEFAULT_SET_SIZE));
        }
    }

    /**
     * Adds a {@link Entity} to the Region. Note that this does not spawn the Entity, or do any other action other than
     * register it to this Region.
     *
     * @param entity The Entity.
     * @param notify A flag indicating whether the {@link RegionListener}s for this Region should be notified.
     * @throws IllegalArgumentException If the Entity does not belong in this Region.
     */
    public void addEntity(Entity entity, boolean notify) {
        Position position = entity.getPosition();
        checkPosition(position);

        Set<Entity> local = entities.computeIfAbsent(position, key -> new HashSet<>(DEFAULT_SET_SIZE));
        local.add(entity);

        if ((System.currentTimeMillis() - start) / 1000 > 10 && (entity instanceof GameObject)) {
            System.out.println("Adding entity " + entity + " to " + entity.getPosition());
        }

        if (notify) {
            notifyListeners(entity, EntityUpdateType.ADD);
        }
    }

    /**
     * Adds a {@link Entity} to the Region. Note that this does not spawn the Entity, or do any other action other than
     * register it to this Region.
     * <p/>
     * By default, this method notifies RegionListeners for this region of the addition.
     *
     * @param entity The Entity.
     * @throws IllegalArgumentException If the Entity does not belong in this Region.
     */
    public void addEntity(Entity entity) {
        addEntity(entity, true);
    }

    /**
     * Checks if this Region contains the specified Entity.
     * <p/>
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
     * Gets a shallow copy of the {@link Set} of {@link Entity}s with the specified {@link EntityType}(s). The returned
     * type will be immutable. Type will be inferred from the call, so ensure that the Entity type and the reference
     * correspond, or this method will fail at runtime.
     *
     * @param position The {@link Position} containing the entities.
     * @param types    The {@link EntityType}s.
     * @return The set of entities.
     */
    public <T extends Entity> Set<T> getEntities(Position position, EntityType... types) {
        Set<Entity> local = entities.get(position);
        if (local == null) {
            return ImmutableSet.of();
        }

        Set<EntityType> set = new HashSet<>(Arrays.asList(types));
        @SuppressWarnings("unchecked")
        Set<T> filtered = (Set<T>) local.stream().filter(entity -> set.contains(entity.getEntityType())).collect(Collectors.toSet());
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
     * Gets a {@link Set} containing {@link RegionUpdateMessage}s that add every {@link Entity} in this Region.
     *
     * @param height The height level to get the Set of RegionUpdateMessages for.
     * @return The Set of RegionUpdateMessages.
     */
    public List<RegionUpdateMessage> getSnapshot(int height) {
        List<RegionUpdateMessage> copy = new ArrayList<>(snapshots.get(height).values());
        Collections.sort(copy);
        return ImmutableList.copyOf(copy);
    }

    /**
     * Gets the updates that have occurred in the last tick in this Region, as a {@link Set} of
     * {@link RegionUpdateMessage}s.
     *
     * @param height The height level to get the Set of RegionUpdateMessages for.
     * @return The Set of RegionUpdateMessages.
     */
    public List<RegionUpdateMessage> getUpdates(int height) {
        List<RegionUpdateMessage> original = this.updates.get(height);
        List<RegionUpdateMessage> updates = new ArrayList<>(original);
        original.clear();

        Collections.sort(updates);
        return ImmutableList.copyOf(updates);
    }

    /**
     * Notifies the {@link RegionListener}s registered to this Region that an update has occurred.
     *
     * @param entity The {@link Entity} that was updated.
     * @param type   The {@link EntityUpdateType} that occurred.
     */
    public void notifyListeners(Entity entity, EntityUpdateType type) {
        listeners.forEach(listener -> listener.execute(this, entity, type));
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
     * @param position  The {@link Position} of the tile.
     * @param entity    The {@link EntityType}.
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
        Preconditions.checkArgument(coordinates.equals(RegionCoordinates.fromPosition(position)), "Position is not included in this Region.");
    }

    /**
     * Records the specified {@link Entity} as being updated this pulse.
     *
     * @param entity The Entity.
     * @param type   The {@link EntityUpdateType}.
     * @throws UnsupportedOperationException If the specified Entity cannot be operated on in this manner.
     */
    private void record(Entity entity, EntityUpdateType type) {
        RegionUpdateMessage message = entity.toUpdateOperation(this, type).toMessage();
        int height = entity.getPosition().getHeight();

        updates.get(height).add(message);
        snapshots.get(height).remove(entity);

        if ((entity.getEntityType() == EntityType.STATIC_OBJECT && type == EntityUpdateType.REMOVE) ||
                (entity.getEntityType() != EntityType.STATIC_OBJECT && type == EntityUpdateType.ADD)) {
            snapshots.get(height).put(entity, message);
        }
    }

}