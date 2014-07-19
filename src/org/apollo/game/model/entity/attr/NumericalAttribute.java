package org.apollo.game.model.entity.attr;

/**
 * An {@link Attribute} with a numerical value.
 * 
 * @author Major
 */
public final class NumericalAttribute extends Attribute<Number> {

	/**
	 * Gets the {@link AttributeType} of number this attribute is.
	 * 
	 * @param value The value of this attribute.
	 * @return The type.
	 */
	private static final AttributeType typeOf(Number value) {
		return value instanceof Double ? AttributeType.DOUBLE : AttributeType.LONG;
	}

	/**
	 * Creates the number attribute.
	 * 
	 * @param value The value of this attribute.
	 */
	public NumericalAttribute(Number value) {
		super(typeOf(value), value);
	}

}