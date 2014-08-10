package org.apollo.game.model.entity;

import org.apollo.game.model.Position;

/**
 * Represents an in-game entity, such as a mob, object, projectile, etc.
 * 
 * @author Major
 */
public abstract class Entity {

	/**
	 * Represents a type of {@link Entity}.
	 * 
	 * @author Major
	 */
	public enum EntityType {

		/**
		 * An item that has been dropped on the ground.
		 */
		DROPPED_ITEM,

		/**
		 * An object appearing in the game world.
		 */
		GAME_OBJECT,

		/**
		 * An npc.
		 */
		NPC,

		/**
		 * A player.
		 */
		PLAYER,

		/**
		 * A projectile (e.g. an arrow).
		 */
		PROJECTILE;

	}

	/**
	 * The position of this entity.
	 */
	protected Position position;

	/**
	 * Creates a new entity with the specified position.
	 * 
	 * @param position The position.
	 */
	public Entity(Position position) {
		this.position = position;
	}

	/**
	 * Gets the {@link EntityType} of this entity.
	 * 
	 * @return The entity type.
	 */
	public abstract EntityType getEntityType();

	/**
	 * Gets the {@link Position} of this entity.
	 * 
	 * @return The position.
	 */
	public final Position getPosition() {
		return position;
	}

}