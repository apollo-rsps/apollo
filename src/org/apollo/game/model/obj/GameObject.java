package org.apollo.game.model.obj;

import org.apollo.game.model.def.ObjectDefinition;

/**
 * Represents an in-game object.
 * 
 * @author Major
 */
public final class GameObject {

	/**
	 * The object definition.
	 */
	private final ObjectDefinition definition;

	/**
	 * Creates the game object.
	 * 
	 * @param definition The object's definition.
	 */
	public GameObject(ObjectDefinition definition) {
		this.definition = definition;
	}

	/**
	 * Gets the object's definition.
	 * 
	 * @return The definition.
	 */
	public ObjectDefinition getDefinition() {
		return definition;
	}

}