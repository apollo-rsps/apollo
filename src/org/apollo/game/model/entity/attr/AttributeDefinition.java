package org.apollo.game.model.entity.attr;

/**
 * A definition for an {@link Attribute}.
 * 
 * @author Major
 *
 * @param <T> The type of attribute.
 */
public final class AttributeDefinition<T> {

	/**
	 * The default value of this definition.
	 */
	private final T defaultValue;

	/**
	 * The persistence state of this definition.
	 */
	private final AttributePersistence persistence;

	/**
	 * The type of this definition.
	 */
	private final AttributeType type;

	/**
	 * Creates the attribute definition.
	 * 
	 * @param defaultValue The default value.
	 * @param persistence The {@link AttributePersistence} state.
	 * @param type The {@link AttributeType}.
	 */
	public AttributeDefinition(T defaultValue, AttributePersistence persistence, AttributeType type) {
		this.defaultValue = defaultValue;
		this.persistence = persistence;
		this.type = type;
	}

	/**
	 * Gets the default value of this attribute definition.
	 * 
	 * @return The default value.
	 */
	public T getDefault() {
		return defaultValue;
	}

	/**
	 * Gets the persistence state of this attribute definition.
	 * 
	 * @return The persistence.
	 */
	public AttributePersistence getPersistence() {
		return persistence;
	}

	/**
	 * Gets the {@link AttributeType} of this definition.
	 * 
	 * @return The attribute type.
	 */
	public AttributeType getType() {
		return type;
	}

}