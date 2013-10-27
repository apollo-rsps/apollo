package org.apollo.game.model.obj;

import org.apollo.game.model.def.StaticObjectDefinition;

/**
 * Represents a static object in the game world.
 * 
 * @author Graham
 */
public final class StaticObject {

	/**
	 * The object definition.
	 */
	private final StaticObjectDefinition definition;

	/**
	 * Creates the game object.
	 * 
	 * @param definition
	 *            The object's definition.
	 */
	public StaticObject(StaticObjectDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Gets the object's definition.
	 * 
	 * @return The definition.
	 */
	public StaticObjectDefinition getDefinition() {
		return definition;
	}

}