package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;

/**
 * Represents an object in the game world.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class GameObject extends Entity {

	/**
	 * The config value that stores the object's id, type, and orientation.
	 */
	private final int config;

	/**
	 * Creates a game object.
	 * 
	 * @param id The object's id.
	 * @param position The position.
	 * @param type The type code of the object.
	 * @param orientation The orientation of the object.
	 */
	public GameObject(int id, Position position, int type, int orientation) {
		super(position);
		this.config = (id * 256) + (type * 4) + orientation;
	}

	/**
	 * Gets the definition of this object.
	 * 
	 * @return The object's definition.
	 */
	public ObjectDefinition getDefinition() {
		return ObjectDefinition.lookup(getId());
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.GAME_OBJECT;
	}

	/**
	 * Gets this object's id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return config / 256;
	}

	/**
	 * Gets this object's orientation.
	 * 
	 * @return The orientation.
	 */
	public int getRotation() {
		return config & 0x3;
	}

	/**
	 * Gets this object's type.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return (config >> 2) & 0x3F;
	}

	@Override
	public String toString() {
		return GameObject.class.getName() + " [id=" + getId() + ", type=" + getType() + ", rotation=" + getRotation()
				+ "]";
	}

}