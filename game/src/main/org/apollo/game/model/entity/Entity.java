package org.apollo.game.model.entity;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;

/**
 * Represents an in-game entity, such as a mob, object, projectile, etc.
 *
 * @author Major
 */
public abstract class Entity {

	/**
	 * The Position of this Entity.
	 */
	protected Position position;

	/**
	 * The World containing this Entity.
	 */
	protected final World world;

	/**
	 * Creates the Entity.
	 *
	 * @param world The {@link World} containing the Entity.
	 * @param position The {@link Position} of the Entity.
	 */
	public Entity(World world, Position position) {
		this.world = world;
		this.position = position;
	}

	@Override
	public abstract boolean equals(Object obj);

	/**
	 * Gets the {@link Position} of this Entity.
	 *
	 * @return The Position.
	 */
	public final Position getPosition() {
		return position;
	}

	/**
	 * Gets the {@link World} this Entity is in.
	 *
	 * @return The World.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Gets the {@link EntityType} of this Entity.
	 *
	 * @return The EntityType.
	 */
	public abstract EntityType getEntityType();

	@Override
	public abstract int hashCode();

}