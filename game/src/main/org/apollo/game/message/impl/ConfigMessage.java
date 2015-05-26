package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to adjust a certain config or attribute setting.
 *
 * @author Chris Fletcher
 */
public final class ConfigMessage extends Message {

	/**
	 * The identifier.
	 */
	private final int id;

	/**
	 * The value.
	 */
	private final int value;

	/**
	 * Creates a new config message.
	 *
	 * @param id The config's identifier.
	 * @param value The value.
	 */
	public ConfigMessage(int id, int value) {
		this.id = id;
		this.value = value;
	}

	/**
	 * Gets the config's identifier.
	 *
	 * @return The config id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the config's value.
	 *
	 * @return The config value.
	 */
	public int getValue() {
		return value;
	}

}