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
	private final AttributeType type;

	/**
	 * The value of this attribute.
	 */
	protected T value;

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

}