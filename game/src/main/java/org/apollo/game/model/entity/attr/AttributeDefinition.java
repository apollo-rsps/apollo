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
	 * Creates an AttributeDefinition for a {@code boolean}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Boolean> forBoolean(boolean defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.BOOLEAN);
	}

	/**
	 * Creates an AttributeDefinition for a {@code double}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Double> forDouble(double defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.DOUBLE);
	}

	/**
	 * Creates an AttributeDefinition for an {@code int}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Integer> forInt(int defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.LONG);
	}

	/**
	 * Creates an AttributeDefinition for a {@code long}.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<Long> forLong(long defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.LONG);
	}

	/**
	 * Creates an AttributeDefinition for a String.
	 *
	 * @param defaultValue The default value of the definition.
	 * @param persistence The {@link AttributePersistence} of the definition.
	 * @return The AttributeDefinition.
	 */
	public static AttributeDefinition<String> forString(String defaultValue, AttributePersistence persistence) {
		return new AttributeDefinition<>(defaultValue, persistence, AttributeType.STRING);
	}

	/**
	 * The default value of the Attribute.
	 */
	private final T defaultValue;

	/**
	 * The persistence state of the Attribute.
	 */
	private final AttributePersistence persistence;

	/**
	 * The type of the Attribute.
	 */
	private final AttributeType type;

	/**
	 * Creates the AttributeDefinition.
	 *
	 * @param defaultValue The default value.
	 * @param persistence The {@link AttributePersistence}.
	 * @param type The {@link AttributeType}.
	 */
	public AttributeDefinition(T defaultValue, AttributePersistence persistence, AttributeType type) {
		this.defaultValue = defaultValue;
		this.persistence = persistence;
		this.type = type;
	}

	/**
	 * Gets the default value of this AttributeDefinition.
	 *
	 * @return The default value.
	 */
	public T getDefault() {
		return defaultValue;
	}

	/**
	 * Gets the {@link AttributePersistence} of this AttributeDefinition.
	 *
	 * @return The AttributePersistence.
	 */
	public AttributePersistence getPersistence() {
		return persistence;
	}

	/**
	 * Gets the {@link AttributeType} of this AttributeDefinition
	 *
	 * @return The AttributeType.
	 */
	public AttributeType getType() {
		return type;
	}

}