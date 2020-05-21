package org.apollo.game.model.entity.setting;

import com.google.common.base.Preconditions;

/**
 * An enumeration representing the brightness of a player's screen. This enumeration relies on the ordering of the
 * elements within, which should be as follows: {@code DARK}, {@code NORMAL}, {@code BRIGHT}, {@code VERY_BRIGHT}.
 *
 * @author Major
 */
public enum ScreenBrightness {

	/**
	 * Represents the 'dark' screen brightness.
	 */
	DARK,

	/**
	 * Represents the 'normal' screen brightness.
	 */
	NORMAL,

	/**
	 * Represents the 'bright' screen brightness.
	 */
	BRIGHT,

	/**
	 * Represents the 'very bright' screen brightness.
	 */
	VERY_BRIGHT;

	/**
	 * Gets the screen brightness for the specified numerical value.
	 *
	 * @param value The numerical value.
	 * @return The screen brightness.
	 * @throws IllegalArgumentException If the specified value is out of bounds.
	 */
	public static ScreenBrightness valueOf(int value) {
		ScreenBrightness[] values = values();
		Preconditions.checkElementIndex(value, values.length, "Invalid screen brightness integer value specified " + value + ".");
		return values[value];
	}

	/**
	 * Converts this screen brightness to an integer.
	 *
	 * @return The numerical value.
	 */
	public int toInteger() {
		return ordinal();
	}

}