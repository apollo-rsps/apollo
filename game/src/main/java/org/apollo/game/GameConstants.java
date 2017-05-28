package org.apollo.game;

/**
 * Contains game-related constants.
 *
 * @author Graham
 */
public final class GameConstants {

	/**
	 * The maximum amount of messages to process per pulse (per session).
	 */
	public static final int MESSAGES_PER_PULSE = 25;

	/**
	 * The delay between consecutive pulses, in milliseconds.
	 */
	public static final int PULSE_DELAY = 600;

	/**
	 * Default private constructor to prevent instantiation by other classes.
	 */
	private GameConstants() {

	}

}