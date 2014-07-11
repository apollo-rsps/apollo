package org.apollo.game.model.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apollo.game.model.Position;

/**
 * Represents an in-game entity, such as a mob, object, projectile etc.
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
	 * A map of attribute names to attributes.
	 */
	protected final Map<String, Object> attributes = new HashMap<>(5);

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
	 * Gets the value of the attribute with the specified name.
	 * 
	 * @param name The name of the attribute.
	 * @return The value of the attribute.
	 */
	public final Object getAttribute(String name) {
		return attributes.get(name);
	}

	/**
	 * Gets all of the attributes of this entity, as a {@link Set} of {@link Entry} objects.
	 * 
	 * @return The set of attributes.
	 */
	public final Set<Entry<String, Object>> getAttributes() {
		return attributes.entrySet();
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
	public Position getPosition() {
		return position;
	}

	/**
	 * Sets the value of the attribute with the specified name.
	 * 
	 * @param name The name of the attribute.
	 * @param value The value of the attribute.
	 */
	public final void setAttribute(String name, Object value) {
		attributes.put(name, value);
	}

}