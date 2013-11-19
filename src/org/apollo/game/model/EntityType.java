package org.apollo.game.model;

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
	 * A temporary object.
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
	PROJECTILE,

	/**
	 * A permanent object appearing on the map.
	 */
	STATIC_OBJECT;

}