package org.apollo.game.model.entity.setting;

import com.google.common.base.Preconditions;

/**
 * An enumeration representing the different privacy states for public, private and trade chat. This enumeration relies
 * on the ordering of the elements within, which should be as follows: {@code ON}, {@code HIDE}, {@code FRIENDS},
 * {@code OFF}, {@code FILTERABLE}.
 *
 * @author Kyle Stevenson
 */
public enum PrivacyState {

	/**
	 * Represents the 'on' state, when all messages are displayed.
	 */
	ON(0),

	/**
	 * Represents the 'hidden' state, when all public chat text is displayed over the heads of players, but not in the
	 * chat interface. This state only applies to public chat.
	 */
	HIDE(1),

	/**
	 * Represents the 'friends' state, when only messages from friends and moderators are displayed.
	 */
	FRIENDS(2),

	/**
	 * Represents the 'off' state, when only messages from moderators are displayed.
	 */
	OFF(3),

	/**
	 * Represents the 'filterable' state - a custom state that filters 'unnecessary' server messages. This state only
	 * applies to public chat.
	 */
	FILTERABLE(4);

	/**
	 * Gets the privacy state for the specified numerical value.
	 *
	 * @param value The numerical value.
	 * @param chat Whether or not the value is for public chat.
	 * @return The privacy state.
	 * @throws IllegalArgumentException If the specified value is out of bounds.
	 */
	public static PrivacyState valueOf(int value, boolean chat) {
		PrivacyState[] values = values();
		if (!chat && value != 0) {
			value++;
		}
		Preconditions.checkElementIndex(value, values.length, "Invalid privacy option integer value specified: " + value + ".");
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

	/*
	 * Horrible workarounds are required here because public chat supports ON, HIDE, FRIENDS, and OFF, but trade and
	 * private only support ON, FRIENDS, and OFF.
	 */

	/**
	 * Converts this privacy state to an integer.
	 *
	 * @param chat Whether or not the value is for public chat.
	 * @return The numerical value used by the client.
	 */
	public int toInteger(boolean chat) {
		if (chat) {
			return value;
		}
		return value == 0 ? 0 : value - 1;
	}

}