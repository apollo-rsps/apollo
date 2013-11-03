package org.apollo.game.sync.block;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;

/**
 * The Force Movement {@link SynchronizationBlock}.
 * 
 * @note This block is used to force a player to walk to a set location. The player can then perform an action (e.g. an
 *       animation), as used in the Agility skill, hence this block earning the name 'Asynchronous Animation/Walking',
 *       although the action is not restricted to animations.
 * 
 * @author Major
 */
public class ForceMovementBlock extends SynchronizationBlock {

	/**
	 * The initial {@link Position} of the player.
	 */
	private final Position initialPosition;

	/**
	 * The {@link Position} the player is being moved to.
	 */
	private final Position finalPosition;

	/**
	 * The length of time (in game ticks) the player's movement along the X axis will last.
	 */
	private final int travelDurationX;

	/**
	 * The length of time (in game ticks) the player's movement along the Y axis will last.
	 */
	private final int travelDurationY;

	/**
	 * The direction the player is moving.
	 */
	private final Direction direction;

	/**
	 * Creates a new Force Movement block.
	 * 
	 * @param initialPosition The initial {@link Position} of the player.
	 * @param finalPosition The final {@link Position} of the player
	 * @param travelDurationX The length of time (in game ticks) the player's movement along the X axis will last.
	 * @param travelDurationY The length of time (in game ticks) the player's movement along the Y axis will last.
	 * @param direction The direction the player should move.
	 */
	public ForceMovementBlock(Position initialPosition, Position finalPosition, int travelDurationX,
			int travelDurationY, Direction direction) {
		this.initialPosition = initialPosition;
		this.finalPosition = finalPosition;
		this.travelDurationX = travelDurationX;
		this.travelDurationY = travelDurationY;
		this.direction = direction;
	}

	/**
	 * Gets the direction the player should move.
	 * 
	 * @return The direction.
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Gets the final position. This shouldn't be used to get the initial X and Y coordinates, see {@link #getFinalX()}
	 * and {@link #getFinalY()}.
	 * 
	 * @return The final {@link Position}.
	 */
	public Position getFinalPosition() {
		return finalPosition;
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
	 * Gets the initial position. This shouldn't be used to get the initial X and Y coordinates, see
	 * {@link #getInitialX()} and {@link #getInitialY()}.
	 * 
	 * @return The initial {@link Position}.
	 */
	public Position getInitialPosition() {
		return initialPosition;
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
	 * Gets the length of time (in game ticks) the player's movement along the Y axis will last.
	 * 
	 * @return The time period.
	 */
	public int getTravelDurationX() {
		return travelDurationX;
	}

	/**
	 * Gets the length of time (in game ticks) the player's movement along the Y axis will last.
	 * 
	 * @return The time period.
	 */
	public int getTravelDurationY() {
		return travelDurationY;
	}

}