package org.apollo.game.model.entity.setting;

import com.google.common.base.Preconditions;

/**
 * Represents the status of the friend server. This enumeration relies on the ordering of the elements within, which
 * should be as follows: {@code OFFLINE}, {@code CONNECTING}, {@code ONLINE}.
 *
 * @author Major
 */
public enum ServerStatus {

	/**
	 * Indicates the friend server is offline.
	 */
	OFFLINE,

	/**
	 * Indicates the friend server is being connected to.
	 */
	CONNECTING,

	/**
	 * Indicates the friend server is online and connected.
	 */
	ONLINE;

	/**
	 * Gets the code of this server status.
	 *
	 * @return The code.
	 */
	public int getCode() {
		return ordinal();
	}

	/**
	 * Gets the server status for the specified numerical value.
	 *
	 * @param value The value.
	 * @return The server status.
	 * @throws IndexOutOfBoundsException If the specified value is out of bounds.
	 */
	public static ServerStatus valueOf(int value) {
		ServerStatus[] values = values();
		Preconditions.checkElementIndex(value, values.length, "Invalid server status integer value supplied " + value + ".");
		return values[value];
	}

}