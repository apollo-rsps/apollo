package org.apollo.game.model.entity.path;

import org.apollo.game.model.Position;

/**
 * The Chebyshev heuristic, ideal for a system that allows for 8-directional movement.
 *
 * @author Major
 */
public final class ChebyshevHeuristic extends Heuristic {

	@Override
	public int estimate(Position current, Position goal) {
		int dx = Math.abs(current.getX() - goal.getX());
		int dy = Math.abs(current.getX() - goal.getY());
		return dx >= dy ? dx : dy;
	}

}