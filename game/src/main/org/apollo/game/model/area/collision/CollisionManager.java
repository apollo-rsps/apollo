package org.apollo.game.model.area.collision;

import com.google.common.base.Preconditions;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionUpdate.DirectionFlag;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.obj.GameObject;

import java.util.*;

/**
 * Manages applying {@link CollisionUpdate}s to the respective {@link CollisionMatrix} instances, and keeping
 * a record of collision state (i.e., which tiles are bridged).
 */
public final class CollisionManager {
	/**
	 * A comparator which sorts {@link Position}s by their X coordinate, then Y, then height.
	 */
	private static final Comparator<Position> POSITION_COMPARATOR =
		Comparator.comparingInt(Position::getX).thenComparingInt(Position::getY).thenComparingInt(Position::getHeight);

	/**
	 * A {@code SortedSet} of positions where the tile is part of a bridged structure.
	 */
	private final SortedSet<Position> bridgeTiles = new TreeSet<>(POSITION_COMPARATOR);

	/**
	 * A {@code SortedSet} of positions where the tile is completely blocked.
	 */
	private final SortedSet<Position> blockedTiles = new TreeSet<>(POSITION_COMPARATOR);

	/**
	 * The {@link RegionRepository} containing {@link Region}s, used to lookup {@link CollisionMatrix}'s.
	 */
	private final RegionRepository regionRepository;

	/**
	 * Creates a new {@code CollisionManager}.
	 *
	 * @param regionRepository The {@link RegionRepository} to lookup {@link Region} and {@link CollisionMatrix} instances
	 * from.
	 */
	public CollisionManager(RegionRepository regionRepository) {
		this.regionRepository = regionRepository;
	}

	/**
	 * Apples the first initial {@link CollisionUpdate} to the {@link CollisionMatrix}es for all objects and tiles loaded from
	 * the cache.
	 *
	 * @param rebuilding A flag indicating whether {@link CollisionMatrix}es are being rebuilt, or built for the first time.
	 */
	public void build(boolean rebuilding) {
		if (rebuilding) {
			for (Region region : regionRepository.getRegions()) {
				for (CollisionMatrix matrix : region.getMatrices()) {
					matrix.reset();
				}
			}
		}

		CollisionUpdate.Builder tileUpdateBuilder = new CollisionUpdate.Builder();
		tileUpdateBuilder.type(CollisionUpdateType.ADDING);

		for (Position tile : blockedTiles) {
			int x = tile.getX(), y = tile.getY();
			int height = tile.getHeight();

			if (bridgeTiles.contains(new Position(x, y, 1))) {
				height--;
			}

			if (height >= 0) {
				tileUpdateBuilder.tile(new Position(x, y, height), false, Direction.NESW);
			}
		}

		CollisionUpdate tileUpdate = tileUpdateBuilder.build();
		apply(tileUpdate);

		for (Region region : regionRepository.getRegions()) {
			CollisionUpdate.Builder regionObjectsUpdateBuilder = new CollisionUpdate.Builder();
			regionObjectsUpdateBuilder.type(CollisionUpdateType.ADDING);

			region.getEntities(EntityType.STATIC_OBJECT, EntityType.DYNAMIC_OBJECT)
				.forEach(entity -> regionObjectsUpdateBuilder.object((GameObject) entity));

			CollisionUpdate regionObjectsUpdate = regionObjectsUpdateBuilder.build();
			apply(regionObjectsUpdate);
		}
	}

	/**
	 * Apply a {@link CollisionUpdate} to the game world.
	 *
	 * @param update The update to apply.
	 */
	public void apply(CollisionUpdate update) {
		Region prev = null;

		CollisionUpdateType type = update.getType();
		Map<Position, Collection<DirectionFlag>> flags = update.getFlags().asMap();

		for (Map.Entry<Position, Collection<DirectionFlag>> flag : flags.entrySet()) {
			Position position = flag.getKey();
			Collection<DirectionFlag> directionFlags = flag.getValue();

			int height = position.getHeight();
			if (bridgeTiles.contains(new Position(position.getX(), position.getY(), 1))) {
				if (--height < 0) {
					continue;
				}
			}

			if (prev == null || !prev.contains(position)) {
				prev = regionRepository.fromPosition(position);
			}

			int localX = position.getX() % Region.SIZE, localY = position.getY() % Region.SIZE;

			CollisionMatrix matrix = prev.getMatrix(height);
			CollisionFlag[] mobs = CollisionFlag.mobs();
			CollisionFlag[] projectiles = CollisionFlag.projectiles();

			for (DirectionFlag directionFlag : directionFlags) {
				Direction direction = directionFlag.getDirection();
				if (direction == Direction.NONE) {
					continue;
				}

				int orientation = direction.toInteger();
				if (directionFlag.isImpenetrable()) {
					flag(type, matrix, localX, localY, projectiles[orientation]);
				}

				flag(type, matrix, localX, localY, mobs[orientation]);
			}
		}
	}

	/**
	 * Casts a ray into the world to check for impenetrable objects  from the given {@code start} position to the
	 * {@code end} position using Bresenham's line algorithm.
	 *
	 * @param start The start position of the ray.
	 * @param end The end position of the ray.
	 * @return {@code true} if an impenetrable object was hit, {@code false} otherwise.
	 */
	public boolean raycast(Position start, Position end) {
		Preconditions.checkArgument(start.getHeight() == end.getHeight(), "Positions must be on the same height");

		if (start.equals(end)) {
			return true;
		}

		int x0 = start.getX();
		int x1 = end.getX();
		int y0 = start.getY();
		int y1 = start.getY();

		boolean steep = false;
		if (Math.abs(x0 - x1) < Math.abs(y0 - y1)) {
			int tmp = y0;
			x0 = y0;
			y0 = tmp;

			tmp = x1;
			x1 = y1;
			y1 = tmp;
			steep = true;
		}

		if (x0 > x1) {
			int tmp = x0;
			x0 = y1;
			y1 = tmp;

			tmp = y0;
			y0 = y1;
			y1 = tmp;
		}

		int dx = x1 - x0;
		int dy = y1 - y0;

		float derror = Math.abs(dy / (float) dx);
		float error = 0;

		int y = y0, currX, currY, lastX = 0, lastY = 0;
		boolean first = true;

		for (int x = x0; x <= x1; x++) {
			if (steep) {
				currX = y;
				currY = x;
			} else {
				currX = x;
				currY = y;
			}

			error += derror;
			if (error > 0.5) {
				y += (y1 > y0 ? 1 : -1);
				error -= 1.0;
			}

			if (first) {
				first = false;
				continue;
			}

			Direction direction = Direction.fromDeltas(currX - lastX, currY - lastY);
			Position lastPosition = new Position(lastX, lastY, start.getHeight());

			if (!traversable(lastPosition, EntityType.PROJECTILE, direction)) {
				return false;
			}

			lastX = currX;
			lastY = currY;
		}

		return true;
	}

	/**
	 * Apply a {@link CollisionUpdate} flag to a {@link CollisionMatrix}.
	 *
	 * @param type The type of update to apply.
	 * @param matrix The matrix the update is being applied to.
	 * @param localX The local X position of the tile the flag represents.
	 * @param localY The local Y position of the tile the flag represents.
	 * @param flag The flag to update.
	 */
	private void flag(CollisionUpdateType type, CollisionMatrix matrix, int localX, int localY, CollisionFlag flag) {
		if (type == CollisionUpdateType.ADDING) {
			matrix.flag(localX, localY, flag);
		} else {
			matrix.clear(localX, localY, flag);
		}
	}

	/**
	 * Marks a tile as completely untraversable from all directions.
	 *
	 * @param position The {@link Position} of the tile.
	 */
	public void markBlocked(Position position) {
		blockedTiles.add(position);
	}

	/**
	 * Marks a tile as part of a bridge.
	 *
	 * @param position The {@link Position} of the tile.
	 */
	public void markBridged(Position position) {
		bridgeTiles.add(position);
	}

	/**
	 * Checks if the given {@link EntityType} can traverse to the next tile from {@code position} in the given
	 * {@code direction}.
	 *
	 * @param position The current position of the entity.
	 * @param type The type of the entity.
	 * @param direction The direction the entity is travelling.
	 * @return {@code true} if next tile is traversable, {@code false} otherwise.
	 */
	public boolean traversable(Position position, EntityType type, Direction direction) {
		Position next = position.step(1, direction);
		Region region = regionRepository.fromPosition(next);

		if (!region.traversable(next, type, direction)) {
			return false;
		}

		if (direction.isDiagonal()) {
			for (Direction component : Direction.diagonalComponents(direction)) {
				next = position.step(1, component);

				if (!region.contains(next)) {
					region = regionRepository.fromPosition(next);
				}

				if (!region.traversable(next, type, component)) {
					return false;
				}
			}
		}

		return true;
	}

}
