package org.apollo.game.model.entity.path;

import org.apollo.game.model.Position;

/**
 * The Manhattan heuristic, ideal for a system that limits movement to 4 directions.
 *
 * @author Major
 */
final class ManhattanHeuristic extends Heuristic {

	@Override
	public int estimate(Position current, Position goal) {
		int dx = Math.abs(current.getX() - goal.getX());
		int dy = Math.abs(current.getX() - goal.getY());
		return dx + dy;
	}

}