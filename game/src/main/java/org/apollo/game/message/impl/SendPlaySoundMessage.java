package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to play a sound.
 *
 * @author tlf30
 */
public final class SendPlaySoundMessage extends Message {

	/**
	 * The id of the sound to play.
	 */
	private final int id;

	/**
	 * The delay that the client should use before playing the sound.
	 * Deplay is measured in milliseconds.
	 */
	private final int delay;

	/**
	 * The type of sound to play.
	 * Usually 0.
	 */
	private final int type;

	/**
	 * Creates a new send play sound message.
	 *
	 * @param id The id of the sound to play.
	 * @param type The type of the sound to play, usually 0.
	 * @param delay The delay in milliseconds before the client plays the sound
	 */
	public SendPlaySoundMessage(int id, int type, int delay) {
		this.id = id;
		this.type = type;
		this.delay = delay;
	}

	/**
	 * Gets the id of the sound.
	 *
	 * @return The id of the sound.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the type of the sound.
	 *
	 * @return The type of the sound.
	 */
	public int getType() {
		return type;
	}

	/**
	 * Gets the number of milliseconds for the client to delay before playing the sound.
	 *
	 * @return The delay of the sound in milliseconds.
	 */
	public int getDelay() {
		return delay;
	}

}
