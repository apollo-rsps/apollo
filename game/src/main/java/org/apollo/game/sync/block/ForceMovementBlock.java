package org.apollo.game.sync.block;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;

/**
 * The Force Movement {@link SynchronizationBlock}. Only players can utilise this block.
 * <p>
 * Note: This block is used to force a player to walk to a set location. The player can then perform an action (e.g. an
 * animation), as used in the Agility skill, hence this block earning the name 'Asynchronous Animation/Walking',
 * although the action is not restricted to animations.
 *
 * @author Major
 */
public final class ForceMovementBlock extends SynchronizationBlock {

	/**
	 * The direction the player is moving.
	 */
	private final Direction direction;

	/**
	 * The {@link Position} the player is being moved to.
	 */
	private final Position finalPosition;

	/**
	 * The initial {@link Position} of the player.
	 */
	private final Position initialPosition;

	/**
	 * The length of time (in game pulses) the player's movement along the X-axis will last.
	 */
	private final int travelDurationX;

	/**
	 * The length of time (in game pulses) the player's movement along the Y-axis will last.
	 */
	private final int travelDurationY;

	/**
	 * Creates a new force movement block.
	 *
	 * @param initialPosition The initial {@link Position} of the player.
	 * @param finalPosition The final {@link Position} of the player
	 * @param travelDurationX The length of time (in game pulses) the player's movement along the X-axis will last.
	 * @param travelDurationY The length of time (in game pulses) the player's movement along the Y-axis will last.
	 * @param direction The direction the player should move.
	 */
	ForceMovementBlock(Position initialPosition, Position finalPosition, int travelDurationX, int travelDurationY, Direction direction) {
		this.initialPosition = initialPosition;
		this.finalPosition = finalPosition;
		this.travelDurationX = travelDurationX;
		this.travelDurationY = travelDurationY;
		this.direction = direction;
	}

	/**
	 * Gets the {@link Direction} the player should move.
	 *
	 * @return The direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Gets the X coordinate of the final {@link Position}.
	 *
	 * @return The X coordinate.
	 */
	public int getFinalX() {
		return finalPosition.getX();
	}

	/**
	 * Gets the Y coordinate of the final {@link Position}.
	 *
	 * @return The Y coordinate.
	 */
	public int getFinalY() {
		return finalPosition.getY();
	}

	/**
	 * Gets the X coordinate of the initial {@link Position}.
	 *
	 * @return The X coordinate.
	 */
	public int getInitialX() {
		return initialPosition.getX();
	}

	/**
	 * Gets the Y coordinate of the initial {@link Position}.
	 *
	 * @return The Y coordinate.
	 */
	public int getInitialY() {
		return initialPosition.getY();
	}

	/**
	 * Gets the length of time (in game pulses) the player's movement along the X-axis will last.
	 *
	 * @return The time period.
	 */
	public int getTravelDurationX() {
		return travelDurationX;
	}

	/**
	 * Gets the length of time (in game pulses) the player's movement along the Y-axis will last.
	 *
	 * @return The time period.
	 */
	public int getTravelDurationY() {
		return travelDurationY;
	}

}