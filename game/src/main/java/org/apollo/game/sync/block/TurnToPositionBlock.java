package org.apollo.game.sync.block;

import org.apollo.game.model.Position;

/**
 * The turn to position {@link SynchronizationBlock}. Both players and npcs can utilise this block.
 *
 * @author Graham
 */
public final class TurnToPositionBlock extends SynchronizationBlock {

	/**
	 * The position of the mob.
	 */
	private final Position mobPosition;

	/**
	 * The position to turn to.
	 */
	private final Position turnPosition;

	/**
	 * Creates the turn to position block.
	 *
	 * @param turnPosition The position to turn to.
	 */
	TurnToPositionBlock(Position mobPosition, Position turnPosition) {
		this.mobPosition = mobPosition;
		this.turnPosition = turnPosition;
	}

	/**
	 * Gets the {@link Position} of the mob.
	 *
	 * @return The position of the mob.
	 */
	public Position getMobPosition() {
		return mobPosition;
	}

	/**
	 * Gets the {@link Position} to turn to.
	 *
	 * @return The position to turn to.
	 */
	public Position getTurnPosition() {
		return turnPosition;
	}

}