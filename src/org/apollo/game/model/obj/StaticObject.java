package org.apollo.game.model.obj;

import org.apollo.game.model.def.StaticObjectDefinition;

/**
 * Represents a static object in the game world.
 * @author Graham
 */
public final class StaticObject {

	/**
	 * The object definition.
	 */
	private final StaticObjectDefinition def;

	/**
	 * Creates the game object.
	 * @param def The object's definition.
	 */
	public StaticObject(StaticObjectDefinition def) {
		this.def = def;
	}

}
