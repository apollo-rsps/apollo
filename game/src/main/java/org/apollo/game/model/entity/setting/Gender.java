package org.apollo.game.model.entity.setting;

/**
 * An enumeration containing the two genders (male and female). This enumeration relies on the ordering of the elements
 * within, which should be as follows: {@code MALE}, {@code FEMALE}.
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