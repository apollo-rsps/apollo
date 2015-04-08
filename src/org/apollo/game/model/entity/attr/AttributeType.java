package org.apollo.game.model.entity.attr;

/**
 * The type of attribute. The functionality of this enum (and other classes) is dependent on the ordering of the values
 * - the expected order is {@link #BOOLEAN}, {@link #DOUBLE}, {@link #LONG}, {@link #STRING}, {@link #SYMBOL}.
 *
 * @author Major
 */
public enum AttributeType {

	/**
	 * The boolean attribute type.
	 */
	BOOLEAN,

	/**
	 * The double attribute type.
	 */
	DOUBLE,

	/**
	 * The long attribute type.
	 */
	LONG,

	/**
	 * The string attribute type.
	 */
	STRING,

	/**
	 * The symbol attribute type.
	 */
	SYMBOL;

	/**
	 * Gets the type with the specified ordinal.
	 *
	 * @param ordinal The ordinal.
	 * @return The type.
	 */
	public static AttributeType valueOf(int ordinal) {
		return values()[ordinal];
	}

	/**
	 * Gets the value of this attribute type.
	 *
	 * @return The value.
	 */
	public int getValue() {
		return ordinal();
	}

}