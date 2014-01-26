package org.apollo.game.model.settings;

/**
 * An enumeration representing
 * 
 * @author Major
 */
public enum ScreenBrightness {

	/**
	 * Represents the 'dark' screen brightness.
	 */
	DARK(0),

	/**
	 * Represents the 'normal' screen brightness.
	 */
	NORMAL(1),

	/**
	 * Represents the 'bright' screen brightness.
	 */
	BRIGHT(2),

	/**
	 * Represents the 'very bright' screen brightness.
	 */
	VERY_BRIGHT(3);

	/**
	 * Gets the screen brightness for the specified numerical value.
	 * 
	 * @param value The numerical value.
	 * @return The screen brightness.
	 * @throws IllegalArgumentException If the numerical value is invalid.
	 */
	public static ScreenBrightness valueOf(int value) {
		ScreenBrightness[] values = values();
		if (value < 0 || value >= values.length) {
			throw new IllegalArgumentException("Invalid screen brightness integer value specified");
		}
		return values[value];
	}

	/**
	 * The numerical value of this brightness.
	 */
	private final int value;

	/**
	 * Creates the screen brightness.
	 * 
	 * @param value The numerical value.
	 */
	private ScreenBrightness(int value) {
		this.value = value;
	}

	/**
	 * Converts this screen brightness to an integer.
	 * 
	 * @return The numerical value.
	 */
	public int toInteger() {
		return value;
	}

}