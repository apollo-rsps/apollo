package org.apollo.game.model.obj;

import org.apollo.game.model.Entity;
import org.apollo.game.model.EntityType;
import org.apollo.game.model.Position;
import org.apollo.game.model.def.ObjectDefinition;

/**
 * Represents a static object in the game world.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class StaticObject extends Entity {

	/**
	 * The object's definition.
	 */
	private final ObjectDefinition definition;

	/**
	 * The object's id.
	 */
	private final int id;

	/**
	 * The object's rotation.
	 */
	private final int rotation;

	/**
	 * The object type.
	 */
	private final int type;

	/**
	 * Creates a new static object.
	 * 
	 * @param def The object's id.
	 * @param position The position.
	 * @param type The type code of the object.
	 * @param rotation The rotation of the object.
	 */
	public StaticObject(int id, Position position, int type, int rotation) {
		this.id = id;
		this.position = position;
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
		return EntityType.STATIC_OBJECT;
	}

	/**
	 * Gets the id of the object.
	 * 
	 * @return The object id.
	 */
	public int getId() {
		return id;
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
		return StaticObject.class.getName() + " [id=" + id + ", type=" + type + ", rotation=" + rotation + "]";
	}

}