package org.apollo.game.model.entity.setting;

import com.google.common.base.Preconditions;

/**
 * An enumeration with the different privilege levels a player can have. This enumeration relies on the ordering of the
 * elements within, which should be as follows: {@code STANDARD}, {@code MODERATOR}, {@code ADMINISTRATOR}.
 *
 * @author Graham
 */
public enum PrivilegeLevel {

	/**
	 * A standard (rights 0) account.
	 */
	STANDARD,

	/**
	 * A player moderator (rights 1) account.
	 */
	MODERATOR,

	/**
	 * An administrator (rights 2) account.
	 */
	ADMINISTRATOR;

	/**
	 * Gets the privilege level for the specified numerical value.
	 *
	 * @param value The numerical value.
	 * @return The privilege level.
	 * @throws IllegalArgumentException If the specified value is out of bounds.
	 */
	public static PrivilegeLevel valueOf(int value) {
		PrivilegeLevel[] values = values();
		Preconditions.checkElementIndex(value, values.length, "Invalid privilege level integer value supplied " + value + ".");
		return values[value];
	}

	/**
	 * Gets the numerical value of this privilege level.
	 *
	 * @return The numerical value used in the protocol.
	 */
	public int toInteger() {
		return ordinal();
	}

}