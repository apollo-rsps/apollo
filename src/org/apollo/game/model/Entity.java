package org.apollo.game.model;

/**
 * Represents an in-game entity, such as a character, object, projectile etc.
 * 
 * @author Major
 */
public abstract class Entity {

	/**
	 * The position of the entity.
	 */
	protected Position position;

	/**
	 * Gets the {@link EntityType} of this entity.
	 * 
	 * @return The type.
	 */
	public abstract EntityType getEntityType();

	/**
	 * Gets the position of this entity.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

}