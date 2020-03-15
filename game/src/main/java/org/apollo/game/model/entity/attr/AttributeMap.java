package org.apollo.game.model.entity.attr;

import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.Map;

/**
 * A {@link Map} wrapper used to store {@link Attribute}s and their {@link AttributeDefinition definitions}.
 *
 * @author Major
 */
public final class AttributeMap {

	/**
	 * The default size of the map.
	 */
	private static final int DEFAULT_MAP_SIZE = 2;

	/**
	 * The map of attribute names to definitions.
	 */
	private static Map<String, AttributeDefinition<?>> definitions = new HashMap<>();

	/**
	 * Registers an {@link AttributeDefinition}.
	 *
	 * @param name The name of the attribute.
	 * @param definition The definition.
	 */
	public static void define(String name, AttributeDefinition<?> definition) {
		definitions.put(name, definition);
	}

	/**
	 * Gets the {@link AttributeDefinition} with the specified name, or {@code null} if it is not defined.
	 *
	 * @param name The name of the attribute.
	 * @return The attribute definition.
	 */
	@SuppressWarnings("unchecked")
	public static <T> AttributeDefinition<T> getDefinition(String name) {
		return (AttributeDefinition<T>) definitions.get(name);
	}

	/**
	 * Gets the {@link AttributeDefinition}s, as a {@link Map}.
	 *
	 * @return The map of attribute names to definitions.
	 */
	public static Map<String, AttributeDefinition<?>> getDefinitions() {
		return new HashMap<>(definitions);
	}

	/**
	 * Returns whether or not an {@link AttributeDefinition} with the specified name exists.
	 *
	 * @param name The name of the AttributeDefinition.
	 * @return {@code true} if the AttributeDefinition exists, {@code false} if not.
	 */
	public static boolean hasDefinition(String name) {
		return definitions.containsKey(name);
	}

	/**
	 * The map of attribute names to attributes.
	 */
	private final Map<String, Attribute<?>> attributes = new HashMap<>(DEFAULT_MAP_SIZE);

	/**
	 * Gets the {@link Attribute} with the specified name.
	 *
	 * @param name The name of the attribute.
	 * @return The attribute.
	 */
	@SuppressWarnings("unchecked")
	public <T> Attribute<T> get(String name) {
		AttributeDefinition<T> definition = getDefinition(name);
		Preconditions.checkNotNull(definition, "Attributes must be defined before their value can be retreived.");

		return (Attribute<T>) attributes.computeIfAbsent(name, key -> createAttribute(definition.getDefault(), definition.getType()));
	}

	/**
	 * Gets a shallow copy of the {@link Map} of {@link Attribute}s.
	 *
	 * @return The attributes.
	 */
	public Map<String, Attribute<?>> getAttributes() {
		return new HashMap<>(attributes);
	}

	/**
	 * Sets the value of the {@link Attribute} with the specified name.
	 *
	 * @param name The name of the attribute.
	 * @param attribute The attribute.
	 */
	public void set(String name, Attribute<?> attribute) {
		Preconditions.checkNotNull(getDefinition(name), "Attributes must be defined before their value can be set.");
		attributes.put(name, attribute);
	}

	/**
	 * Creates an {@link Attribute} with the specified value and {@link AttributeType}.
	 *
	 * @param value The value of the Attribute.
	 * @param type The AttributeType.
	 * @return The Attribute.
	 */
	private <T> Attribute<?> createAttribute(T value, AttributeType type) {
		switch (type) {
			case LONG:
				return new NumericalAttribute(((Number) value).longValue());
			case DOUBLE:
				return new NumericalAttribute((Double) value);
			case STRING:
				return new StringAttribute((String) value);
			case BOOLEAN:
				return new BooleanAttribute((Boolean) value);
		}

		throw new IllegalArgumentException("Unrecognised type " + type + ".");
	}

}