package org.apollo.game.model.entity;

import org.apollo.net.message.Message;

/**
 * The type Sound.
 *
 * @author Khaled Abdeljaber
 */
public class Sound extends Message {

	/**
	 * The identifier of the sound.
	 */
	private final int id;

	/**
	 * The volume of the sound.
	 */
	private final int volume;

	/**
	 * The delay of the sound in client ticks.
	 */
	private final int delay;

	/**
	 * Instantiates a new Sound.
	 *
	 * @param id     the id
	 * @param volume the volume
	 * @param delay  the delay
	 */
	public Sound(int id, int volume, int delay) {
		this.id = id;
		this.volume = volume;
		this.delay = delay;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets volume.
	 *
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * Gets delay.
	 *
	 * @return the delay
	 */
	public int getDelay() {
		return delay;
	}
}
