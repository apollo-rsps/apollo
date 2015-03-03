package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;

import com.google.common.base.MoreObjects;

/**
 * Represents an object in the game world.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class GameObject extends Entity {

	/**
	 * The packed value that stores this object's id, type, and orientation.
	 */
	private final int packed;

	/**
	 * Creates the game object.
	 * 
	 * @param id The object's id.
	 * @param position The position.
	 * @param type The type code of the object.
	 * @param orientation The orientation of the object.
	 */
	public GameObject(int id, Position position, int type, int orientation) {
		super(position);
		this.packed = id << 8 | type << 2 | orientation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GameObject) {
			GameObject other = (GameObject) obj;
			return position.equals(other.position) && packed == other.packed;
		}

		return false;
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
		return packed >> 8;
	}

	/**
	 * Gets this object's orientation.
	 * 
	 * @return The orientation.
	 */
	public int getOrientation() {
		return packed & 0x3;
	}

	/**
	 * Gets this object's type.
	 * 
	 * @return The type.
	 */
	public int getType() {
		return (packed >> 2) & 0x3F;
	}

	@Override
	public int hashCode() {
		return packed;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", getId()).add("type", getType()).add("orientation", getOrientation()).toString();
	}

}