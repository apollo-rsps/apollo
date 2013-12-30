package org.apollo.game.model.obj;

import org.apollo.game.model.Entity;
import org.apollo.game.model.EntityType;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;

/**
 * Represents an in-game object.
 * 
 * @author Major
 */
public final class GameObject extends Entity {

	/**
	 * The object definition.
	 */
	private final ObjectDefinition definition;

	/**
	 * Creates the game object.
	 * 
	 * @param definition The object's definition.
	 */
	public GameObject(ObjectDefinition definition, Position position) {
		super(position);
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

	@Override
	public EntityType getEntityType() {
		return EntityType.GAME_OBJECT;
	}

}