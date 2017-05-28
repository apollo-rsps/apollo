package org.apollo.game.model.entity.attr;

/**
 * An attribute belonging to an entity.
 *
 * @author Major
 *
 * @param <T> The type of attribute.
 */

public abstract class Attribute<T> {

	/**
	 * The type of this attribute.
	 */
	protected final AttributeType type;

	/**
	 * The value of this attribute.
	 */
	protected final T value;

	/**
	 * Creates the attribute with the specified {@link AttributeType} and value.
	 *
	 * @param type The type.
	 * @param value The value.
	 */
	protected Attribute(AttributeType type, T value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Encodes this Attribute into a byte array.
	 *
	 * @return The byte array.
	 */
	public abstract byte[] encode();

	/**
	 * Gets the type of this attribute.
	 *
	 * @return The type.
	 */
	public AttributeType getType() {
		return type;
	}

	/**
	 * Gets the value of this attribute.
	 *
	 * @return The value.
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Returns the Sting representation of this Attribute. Will be used to write this Attribute as a String, if
	 * required.
	 */
	@Override
	public abstract String toString();

}