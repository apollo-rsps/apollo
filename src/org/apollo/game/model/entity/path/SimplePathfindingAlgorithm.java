package org.apollo.game.model.entity.path;

import java.util.ArrayDeque;
import java.util.Deque;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;

/**
 * A very simple pathfinding algorithm that simply walks in the direction of the target until it either reaches it or is
 * blocked.
 *
 * @author Major
 */
final class SimplePathfindingAlgorithm extends PathfindingAlgorithm {

	@Override
	public Deque<Position> find(Position origin, Position target) {
		int approximation = (int) (origin.getLongestDelta(target) * 1.5);
		Deque<Position> positions = new ArrayDeque<>(approximation);

		return addHorizontal(origin, target, positions);
	}

	/**
	 * Adds the necessary and possible horizontal {@link Position}s to the existing {@link Deque}.
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Adds positions horizontally until we are either horizontally aligned with the target, or the next step is not
	 * traversable.
	 * <li>Checks if we are not at the target, and that either of the horizontally-adjacent positions are traversable:
	 * if so, we traverse horizontally (see {@link #addHorizontal}); if not, return the current path.
	 * </ul>
	 * 
	 * @param current The current position.
	 * @param target The target position.
	 * @param positions The deque of positions.
	 * @return The deque of positions containing the path.
	 */
	private Deque<Position> addHorizontal(Position current, Position target, Deque<Position> positions) {
		int x = current.getX(), y = current.getY(), height = current.getHeight();
		int dx = x - target.getX();

		if (dx > 0) {
			Position west = new Position(x - 1, y, height);

			while (traversable(west) && dx-- > 0) {
				west = new Position(--x, y, height);
				positions.addLast(west);
			}
		} else if (dx < 0) {
			Position east = new Position(x + 1, y, height);

			while (traversable(east) && dx++ < 0) {
				east = new Position(++x, y, height);
				positions.addLast(east);
			}
		}

		Position last = new Position(x, y, height);
		if (!current.equals(last) && traversable(last, Direction.NORTH, Direction.SOUTH)) {
			return addVertical(last, target, positions);
		}

		return positions;
	}

	/**
	 * Adds the necessary and possible vertical {@link Position}s to the existing {@link Deque}.
	 * <p>
	 * This method:
	 * <ul>
	 * <li>Adds positions vertically until we are either vertically aligned with the target, or the next step is not
	 * traversable.
	 * <li>Checks if we are not at the target, and that either of the horizontally-adjacent positions are traversable:
	 * if so, we traverse horizontally (see {@link #addHorizontal}); if not, return the current path.
	 * </ul>
	 * 
	 * @param current The current position.
	 * @param target The target position.
	 * @param positions The deque of positions.
	 * @return The deque of positions containing the path.
	 */
	private Deque<Position> addVertical(Position current, Position target, Deque<Position> positions) {
		int x = current.getX(), y = current.getY(), height = current.getHeight();
		int dy = y - target.getY();

		if (dy > 0) {
			Position south = new Position(x, y - 1, height);

			while (traversable(south) && dy-- > 0) {
				south = new Position(x, --y, height);
				positions.addLast(south);
			}
		} else if (dy < 0) {
			Position north = new Position(x, y + 1, height);

			while (traversable(north) && dy++ < 0) {
				north = new Position(x, ++y, height);
				positions.addLast(north);
			}
		}

		Position last = new Position(x, y, height);
		if (!last.equals(target) && traversable(last, Direction.EAST, Direction.WEST)) {
			return addHorizontal(last, target, positions);
		}

		return positions;
	}

}