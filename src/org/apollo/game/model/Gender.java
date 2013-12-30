package org.apollo.game.model;

/**
 * An enumeration containing the two genders (male and female).
 * 
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
	 * The numerical value used by the client.
	 */
	private final int value;

	/**
	 * Creates the gender.
	 * 
	 * @param value The numerical value.
	 */
	private Gender(int value) {
		this.value = value;
	}

	/**
	 * Converts this gender to an integer.
	 * 
	 * @return The numerical value used by the client.
	 */
	public int toInteger() {
		return value;
	}

}