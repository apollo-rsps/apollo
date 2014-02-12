package org.apollo.game.model.settings;

/**
 * An enumeration with the different privilege levels a player can have.
 * 
 * @author Graham
 */
public enum PrivilegeLevel {

	/**
	 * A standard (rights 0) account.
	 */
	STANDARD(0),

	/**
	 * A player moderator (rights 1) account.
	 */
	MODERATOR(1),

	/**
	 * An administrator (rights 2) account.
	 */
	ADMINISTRATOR(2);

	/**
	 * Gets the privilege level for the specified numerical level.
	 * 
	 * @param value The numerical level.
	 * @return The privilege level.
	 * @throws IllegalArgumentException If the numerical level is invalid.
	 */
	public static PrivilegeLevel valueOf(int value) {
		PrivilegeLevel[] values = values();
		if (value < 0 || value >= values.length) {
			throw new IndexOutOfBoundsException("Invalid privilege level integer value supplied");
		}
		return values[value];
	}

	/**
	 * The numerical level used in the protocol.
	 */
	private final int value;

	/**
	 * Creates the privilege level.
	 * 
	 * @param value The numerical level.
	 */
	private PrivilegeLevel(int value) {
		this.value = value;
	}

	/**
	 * Gets the numerical level.
	 * 
	 * @return The numerical level used in the protocol.
	 */
	public int toInteger() {
		return value;
	}

}