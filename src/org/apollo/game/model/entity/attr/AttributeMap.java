package org.apollo.game.model.entity.attr;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

/**
 * A {@link Map} wrapper used to store {@link Attribute}s and their {@link AttributeDefinition definitions}.
 * 
 * @author Major
 */
public final class AttributeMap {

	/**
	 * The map of attribute names to definitions.
	 */
	private static Map<String, AttributeDefinition<?>> definitions = new HashMap<>(1);

	/**
	 * Registers an {@link AttributeDefinition}.
	 * 
	 * @param name The name of the attribute.
	 * @param definition The definition.
	 */
	public static void addDefinition(String name, AttributeDefinition<?> definition) {
		definitions.put(name, definition);
	}

	/**
	 * Gets the {@link AttributeDefinition} with the specified name, or {@code null} if it is not defined.
	 * 
	 * @param name The name of the attribute.
	 * @return The attribute definition.
	 */
	public static AttributeDefinition<?> getDefinition(String name) {
		return definitions.get(name);
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
	 * The map of attribute names to attributes.
	 */
	private Map<String, Attribute<?>> attributes = new HashMap<>();

	/**
	 * Gets the {@link Attribute} with the specified name.
	 * 
	 * @param name The name of the attribute.
	 * @return The attribute.
	 */
	public Attribute<?> getAttribute(String name) {
		return attributes.get(name);
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
	public void setAttribute(String name, Attribute<?> attribute) {
		Preconditions.checkNotNull(getDefinition(name), "Attributes must be defined before their value can be set.");
		attributes.put(name, attribute);
	}

}