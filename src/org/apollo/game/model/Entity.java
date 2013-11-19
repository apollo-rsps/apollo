package org.apollo.game.model;


/**
 * Represents an in-game entity, such as a character, object, projectile etc.
 * 
 * @author Major
 */
public interface Entity {

	/**
	 * Gets the {@link EntityType} of this entity.
	 * 
	 * @return The type.
	 */
	public EntityType getEntityType();

	/**
	 * Gets the position of this entity.
	 * 
	 * @return The position.
	 */
	public Position getPosition();

}