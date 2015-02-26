package org.apollo.game.model.entity.path;

import java.util.Deque;
import java.util.Set;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Sector;
import org.apollo.game.model.area.SectorRepository;
import org.apollo.game.model.entity.Entity.EntityType;
import org.apollo.game.model.entity.GameObject;

/**
 * An algorithm used to find a path between two {@link Position}s.
 *
 * @author Major
 */
abstract class PathfindingAlgorithm {

	/**
	 * The repository of sectors.
	 */
	private static final SectorRepository repository = World.getWorld().getSectorRepository();

	/**
	 * Finds a valid path from the origin {@link Position} to the target one.
	 * 
	 * @param origin The origin position.
	 * @param target The target position.
	 * @return The {@link Deque} containing the positions to go through.
	 */
	public abstract Deque<Position> find(Position origin, Position target);

	/**
	 * Returns whether or not the tile at the specified position is walkable. FIXME do this properly w/tile collision
	 * data!
	 *
	 * @param position The {@link Position}.
	 * @return {@code true} if the tile is walkable, otherwise {@code false}.
	 */
	protected boolean traversable(Position position) {
		Sector sector = repository.get(position.getSectorCoordinates());
		Set<GameObject> objects = sector.getEntities(position, EntityType.GAME_OBJECT);

		return objects.stream().anyMatch(object -> object.getDefinition().isSolid());
	}

	/**
	 * Returns whether or not the {@link Position}s walking one step in a specified {@link Direction} would lead to is
	 * traversable.
	 * 
	 * @param position The starting position.
	 * @param directions The directions that should be checked.
	 * @return {@code true} if any of the directions lead to a traversable tile, otherwise {@code false}.
	 */
	protected boolean traversable(Position position, Direction... directions) {
		int height = position.getHeight();

		for (Direction direction : directions) {
			int x = position.getX(), y = position.getY();
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

			if (traversable(new Position(x, y, height))) {
				return true;
			}
		}

		return false;
	}

}