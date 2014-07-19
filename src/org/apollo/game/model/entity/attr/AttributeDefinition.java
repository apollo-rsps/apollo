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
	 * The persistence state of an attribute - either {@code PERSISTENT} (saved) or {@code TRANSIENT} (not saved).
	 */
	public enum Persistence {

		/**
		 * The serialized persistence type, indicating that the attribute will be saved.
		 */
		SERIALIZED,

		/**
		 * The transient persistence type, indicating that the attribute will not be saved.
		 */
		TRANSIENT;

	}

	/**
	 * The default value of this definition.
	 */
	private final T defaultValue;

	/**
	 * The persistence state of this definition.
	 */
	private final Persistence persistence;

	/**
	 * The type of this definition.
	 */
	private final AttributeType type;

	/**
	 * Creates the attribute definition.
	 * 
	 * @param defaultValue The default value.
	 * @param persistence The {@link Persistence} state.
	 * @param type The {@link AttributeType}.
	 */
	public AttributeDefinition(T defaultValue, Persistence persistence, AttributeType type) {
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
	public Persistence getPersistence() {
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