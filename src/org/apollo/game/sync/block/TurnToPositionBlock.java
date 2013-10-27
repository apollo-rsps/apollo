package org.apollo.game.sync.block;

import org.apollo.game.model.Position;

/**
 * The turn to position {@link SynchronizationBlock}.
 * @author Graham
 */
public final class TurnToPositionBlock extends SynchronizationBlock {

	/**
	 * The position to turn to.
	 */
	private final Position position;

	/**
	 * Creates the turn to position block.
	 * @param position The position to turn to.
	 */
	public TurnToPositionBlock(Position position) {
		this.position = position;
	}

	/**
	 * Gets the position to turn to.
	 * @return The position to turn to.
	 */
	public Position getPosition() {
		return position;
	}

}
