package org.apollo.game.model.entity.path;

import java.util.Deque;
import java.util.Optional;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionManager;
import org.apollo.game.model.entity.EntityType;

import com.google.common.base.Preconditions;

/**
 * An algorithm used to find a path between two {@link Position}s.
 *
 * @author Major
 */
abstract class PathfindingAlgorithm {

	private final CollisionManager collisionManager;

	/**
	 * Creates the PathfindingAlgorithm.
	 *
	 * @param collisionManager The {@link CollisionManager} used to check if there is a collision
	 * between two {@link Position}s in a path.
	 */
	public PathfindingAlgorithm(CollisionManager collisionManager) {
		this.collisionManager = collisionManager;
	}

	/**
	 * Finds a valid path from the origin {@link Position} to the target one.
	 *
	 * @param origin The origin Position.
	 * @param target The target Position.
	 * @return The {@link Deque} containing the Positions to go through.
	 */
	public abstract Deque<Position> find(Position origin, Position target);

	/**
	 * Returns whether or not a {@link Position} walking one step in any of the specified {@link Direction}s would lead
	 * to is traversable.
	 *
	 * @param current The current Position.
	 * @param directions The Directions that should be checked.
	 * @return {@code true} if any of the Directions lead to a traversable tile, otherwise {@code false}.
	 */
	protected boolean traversable(Position current, Direction... directions) {
		return traversable(current, Optional.empty(), directions);
	}

	/**
	 * Returns whether or not a {@link Position} walking one step in any of the specified {@link Direction}s would lead
	 * to is traversable.
	 *
	 * @param current The current Position.
	 * @param boundaries The {@link Optional} containing the Position boundaries.
	 * @param directions The Directions that should be checked.
	 * @return {@code true} if any of the Directions lead to a traversable tile, otherwise {@code false}.
	 */
	protected boolean traversable(Position current, Optional<Position[]> boundaries, Direction... directions) {
		Preconditions.checkArgument(directions != null && directions.length > 0, "Directions array cannot be null.");
		int height = current.getHeight();

		Position[] positions = boundaries.isPresent() ? boundaries.get() : new Position[0];

		for (Direction direction : directions) {
			int x = current.getX(), y = current.getY();
			int value = direction.toInteger();

			if (value >= Direction.NORTH_WEST.toInteger() && value <= Direction.NORTH_EAST.toInteger()) {
				y++;
			} else if (value >= Direction.SOUTH_WEST.toInteger() && value <= Direction.SOUTH_EAST.toInteger()) {
				y--;
			}

			if (direction == Direction.NORTH_EAST || direction == Direction.EAST || direction == Direction.SOUTH_EAST) {
				x++;
			} else if (direction == Direction.NORTH_WEST || direction == Direction.WEST || direction == Direction.SOUTH_WEST) {
				x--;
			}

			if (collisionManager.traversable(current, EntityType.NPC, direction)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns whether or not the specified {@link Position} is inside the specified {@code boundary}.
	 *
	 * @param position The Position.
	 * @param boundary The boundary Positions.
	 * @return {@code true} if the specified Position is inside the boundary, {@code false} if not.
	 */
	private boolean inside(Position position, Position[] boundary) {
		int x = position.getX(), y = position.getY();
		Position min = boundary[0], max = boundary[1];

		return x >= min.getX() && y >= min.getY() && x <= max.getX() && y <= max.getY();
	}

}