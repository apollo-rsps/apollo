package org.apollo.game.model;

/**
 * An enumeration representing the different privacy states for public, private and trade chat.
 * 
 * @author Kyle Stevenson
 */
public enum PrivacyState {

	/**
	 * Represents the 'friends' state, when only messages from friends and moderators are displayed.
	 */
	FRIENDS(2),

	/**
	 * Represents the 'hidden' state, when all public chat text is displayed over the heads of players, but not in the
	 * chat interface. This state only applies to public chat.
	 */
	HIDE(1),

	/**
	 * Represents the 'off' state, when only messages from moderators are displayed.
	 */
	OFF(3),

	/**
	 * Represents the 'on' state, when all messages are displayed.
	 */
	ON(0);

	/**
	 * Gets the privacy state for the specified numerical value.
	 * 
	 * @param value The numerical value.
	 * @return The privacy state.
	 * @throws IllegalArgumentException If the numerical value is invalid.
	 */
	public static PrivacyState valueOf(int value) {
		PrivacyState[] values = values();
		if (value < 0 || value >= values.length) {
			throw new IllegalArgumentException("Invalid privacy option integer value specified");
		}
		return values[value];
	}

	/**
	 * The numerical value used by the client.
	 */
	private final int value;

	/**
	 * Creates the privacy state.
	 * 
	 * @param value The numerical value.
	 */
	private PrivacyState(int value) {
		this.value = value;
	}

	/**
	 * Converts this privacy state to an integer.
	 * 
	 * @return The numerical value used by the client.
	 */
	public int toInteger() {
		return value;
	}

}