package org.apollo.game.model.entity;

/**
 * A map of animations for {@link Player}s. Contains animations such as standing, turning, running, etc.
 *
 * @author Steve Soltys
 */
public class AnimationMap {
	/**
	 * The default animation set for {@link Player}s.
	 */
	public static final AnimationMap DEFAULT_ANIMATION_SET = new AnimationMap(0x328, 0x337, 0x333, 0x334, 0x335, 0x336, 0x338);

	/**
	 * The animation for standing in place.
	 */
	private int stand;

	/**
	 * The animation for turning while idle.
	 */
	private int idleTurn;

	/**
	 * The animation for walking.
	 */
	private int walking;

	/**
	 * The animation for turning 180 degrees.
	 */
	private int turnAround;

	/**
	 * The animation for turning 90 degrees right.
	 */
	private int turnRight;

	/**
	 * The animation for turning 90 degrees left.
	 */
	private int turnLeft;

	/**
	 * The animation for running.
	 */
	private int running;

	public AnimationMap(int stand, int idleTurn, int walking, int turnAround, int turnRight, int turnLeft, int running) {
		this.stand = stand;
		this.idleTurn = idleTurn;
		this.walking = walking;
		this.turnAround = turnAround;
		this.turnRight = turnRight;
		this.turnLeft = turnLeft;
		this.running = running;
	}

	/**
	 * Duplicates this animation set.
	 *
	 * @return the duplicated animation set.
	 */
	@Override
	public AnimationMap clone() {
		return new AnimationMap(stand, idleTurn, walking, turnAround, turnRight, turnLeft, running);
	}

	/**
	 * Gets the animation for standing in place.
	 *
	 * @return the animation.
	 */
	public int getStand() {
		return stand;
	}

	/**
	 * Sets the animation for standing in place.
	 *
	 * @param stand the animation.
	 */
	public void setStand(int stand) {
		this.stand = stand;
	}

	/**
	 * Gets the animation for turning while idle.
	 *
	 * @return the animation.
	 */
	public int getIdleTurn() {
		return idleTurn;
	}

	/**
	 * Sets the animation for standing in place.
	 *
	 * @param idleTurn the animation.
	 */
	public void setIdleTurn(int idleTurn) {
		this.idleTurn = idleTurn;
	}

	/**
	 * Gets the animation for walking.
	 *
	 * @return the animation.
	 */
	public int getWalking() {
		return walking;
	}

	/**
	 * Sets the animation for walking.
	 *
	 * @param walking the animation.
	 */
	public void setWalking(int walking) {
		this.walking = walking;
	}

	/**
	 * Gets the animation for turning 180 degrees.
	 *
	 * @return the animation.
	 */
	public int getTurnAround() {
		return turnAround;
	}

	/**
	 * Sets the animation for turning 180 degrees.
	 *
	 * @param turnAround the animation.
	 */
	public void setTurnAround(int turnAround) {
		this.turnAround = turnAround;
	}

	/**
	 * Gets the animation for turning 90 degrees right.
	 *
	 * @return the animation.
	 */
	public int getTurnRight() {
		return turnRight;
	}

	/**
	 * Sets the animation for turning 90 degrees right.
	 *
	 * @param turnRight the animation.
	 */
	public void setTurnRight(int turnRight) {
		this.turnRight = turnRight;
	}

	/**
	 * Gets the animation for turning 90 degrees left.
	 *
	 * @return the animation.
	 */
	public int getTurnLeft() {
		return turnLeft;
	}

	/**
	 * Sets the animation for turning 90 degrees left.
	 *
	 * @param turnLeft the animation.
	 */
	public void setTurnLeft(int turnLeft) {
		this.turnLeft = turnLeft;
	}

	/**
	 * Gets the animation for running.
	 *
	 * @return the animation.
	 */
	public int getRunning() {
		return running;
	}

	/**
	 * Sets the animation for running.
	 *
	 * @param running the animation.
	 */
	public void setRunning(int running) {
		this.running = running;
	}
}
