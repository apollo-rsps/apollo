package org.apollo.game.model.obj;

import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.Entity;

/**
 * Represents an object in the game world.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class GameObject extends Entity {

	/**
	 * The object's definition.
	 */
	private final ObjectDefinition definition;

	/**
	 * The object's rotation.
	 */
	private final int rotation;

	/**
	 * The object type.
	 */
	private final int type;

	/**
	 * Creates a game object.
	 * 
	 * @param id The object's id.
	 * @param position The position.
	 * @param type The type code of the object.
	 * @param rotation The rotation of the object.
	 */
	public GameObject(int id, Position position, int type, int rotation) {
		super(position);
		this.type = type;
		this.rotation = rotation;
		definition = ObjectDefinition.lookup(id);
	}

	/**
	 * Gets the definition of this object.
	 * 
	 * @return The object's definition.
	 */
	public ObjectDefinition getDefinition() {
		return definition;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GAME_OBJECT;
	}

	/**
	 * Gets the object's rotation.
	 * 
	 * @return The rotation.
	 */
	public int getRotation() {
		return rotation;
	}

	/**
	 * Gets the type code of the object.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return type;
	}

	@Override
	public String toString() {
		return GameObject.class.getName() + " [id=" + definition.getId() + ", type=" + type + ", rotation=" + rotation
				+ "]";
	}

}