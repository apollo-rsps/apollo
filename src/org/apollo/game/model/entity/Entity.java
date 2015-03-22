package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.area.EntityUpdateType;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.update.UpdateOperation;

/**
 * Represents an in-game entity, such as a mob, object, projectile, etc.
 * 
 * @author Major
 */
public abstract class Entity {

	/**
	 * Represents a type of {@link Entity}.
	 */
	public enum EntityType {

		/**
		 * An Item that is positioned on the ground.
		 */
		GROUND_ITEM,

		/**
		 * A GameObject that is loaded statically (i.e. from the game resources) at start-up.
		 */
		STATIC_OBJECT,

		/**
		 * A GameObject that is loaded dynamically, usually for specific Players.
		 */
		DYNAMIC_OBJECT,

		/**
		 * An Npc.
		 */
		NPC,

		/**
		 * A Player.
		 */
		PLAYER,

		/**
		 * A projectile (e.g. an arrow).
		 */
		PROJECTILE;

		/**
		 * Returns whether or not this EntityType is for a Mob.
		 * 
		 * @return {@code true} if this EntityType is for a Mob, otherwise {@code false}.
		 */
		public boolean isMob() {
			return this == PLAYER || this == NPC;
		}

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

	@Override
	public abstract boolean equals(Object obj);

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

	@Override
	public abstract int hashCode();

	/**
	 * Gets this Entity, as an {@link UpdateOperation} of a {@link Region}.
	 * 
	 * @param region The Region.
	 * @param type The EntityUpdateType.
	 * @return The UpdateOperation.
	 */
	public abstract UpdateOperation<?> toUpdateOperation(Region region, EntityUpdateType type);

}