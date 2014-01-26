package org.apollo.game.model.settings;

/**
 * An enumeration containing the two genders (male and female).
 * 
 * @author Graham
 */
public enum Gender {

	/**
	 * The male gender.
	 */
	MALE,

	/**
	 * The female gender.
	 */
	FEMALE;

	/**
	 * Converts this gender to an integer.
	 * 
	 * @return The numerical value used by the client.
	 */
	public int toInteger() {
		return ordinal();
	}

}