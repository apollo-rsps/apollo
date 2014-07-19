package org.apollo.game.model.entity;

import java.util.Map;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.attr.Attribute;
import org.apollo.game.model.entity.attr.AttributeMap;

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
	 * The attribute map of this entity.
	 */
	protected final AttributeMap attributes = new AttributeMap();

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
	public final Attribute<?> getAttribute(String name) {
		return attributes.getAttribute(name);
	}

	/**
	 * Gets a shallow copy of the attributes of this entity, as a {@link Map}.
	 * 
	 * @return The map of attributes.
	 */
	public final Map<String, Attribute<?>> getAttributes() {
		return attributes.getAttributes();
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
	 * @param value The attribute.
	 */
	public final void setAttribute(String name, Attribute<?> value) {
		attributes.setAttribute(name, value);
	}

}