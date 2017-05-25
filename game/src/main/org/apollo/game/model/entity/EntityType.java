package org.apollo.game.model.entity;

/**
 * Represents a type of {@link Entity}.
 *
 * @author Major
 */
public enum EntityType {

	/**
	 * A GameObject that is loaded dynamically, usually for specific Players.
	 */
	DYNAMIC_OBJECT,

	/**
	 * An Item that is positioned on the ground.
	 */
	GROUND_ITEM,

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
	PROJECTILE,

	/**
	 * A GameObject that is loaded statically (i.e. from the game resources) at start-up.
	 */
	STATIC_OBJECT;

	/**
	 * Returns whether or not this EntityType is for a Mob.
	 *
	 * @return {@code true} if this EntityType is for a Mob, otherwise {@code false}.
	 */
	public boolean isMob() {
		return this == PLAYER || this == NPC;
	}

	/**
	 * Returns whether or not this EntityType should be short-lived (i.e. not added to its regions local objects).
	 *
	 * @return {@code true} if this EntityType is short-lived.
	 */
	public boolean isTransient() {
		return this == PROJECTILE;
	}
}