package org.apollo.game.model;

import java.io.Serializable;

/**
 * Represents an in-game entity, such as a mob, object, projectile etc.
 * 
 * @author Major
 */
public abstract class Entity implements Serializable {

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
	 * The generated serial UID.
	 */
	private static final long serialVersionUID = 5968243763380631014L;

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