package org.apollo.game.model;

/**
 * Represents an in-game entity, such as a mob, object, projectile etc.
 * 
 * @author Major
 */
public abstract class Entity {

	/**
	 * The position of this entity.
	 */
	protected Position position;

	/**
	 * Gets the {@link EntityType} of this entity.
	 * 
	 * @return The type.
	 */
	public abstract EntityType getEntityType();

	/**
	 * Gets the {@link Position} of this entity.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

}