package org.apollo.game.model;

/**
 * An enumeration containing the two genders (male and female).
 * @author Graham
 */
public enum Gender {

	/**
	 * The male gender.
	 */
	MALE(0),

	/**
	 * The female gender.
	 */
	FEMALE(1);

	/**
	 * An integer representation used by the client.
	 */
	private final int intValue;

	/**
	 * Creates the gender.
	 * @param intValue The integer representation.
	 */
	private Gender(int intValue) {
		this.intValue = intValue;
	}

	/**
	 * Converts this gender to an integer.
	 * @return The integer representation used by the client.
	 */
	public int toInteger() {
		return intValue;
	}

}
