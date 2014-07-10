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
     * The object's definition.
     */
    private final ObjectDefinition definition;

    /**
     * The object's orientation.
     */
    private final int orientation;

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
     * @param orientation The orientation of the object.
     */
    public GameObject(int id, Position position, int type, int orientation) {
	super(position);
	this.type = type;
	this.orientation = orientation;
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
     * Gets this object's id.
     * 
     * @return The id.
     */
    public int getId() {
	return definition.getId();
    }

    /**
     * Gets this object's orientation.
     * 
     * @return The orientation.
     */
    public int getRotation() {
	return orientation;
    }

    /**
     * Gets this object's type.
     * 
     * @return The type.
     */
    public int getType() {
	return type;
    }

    @Override
    public String toString() {
	return GameObject.class.getName() + " [id=" + definition.getId() + ", type=" + type + ", rotation="
		+ orientation + "]";
    }

}