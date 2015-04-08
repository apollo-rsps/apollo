package org.apollo.game.sync.seg;

/**
 * An enumeration which contains the types of segments.
 *
 * @author Graham
 */
public enum SegmentType {

	/**
	 * A segment where the mob is added.
	 */
	ADD_MOB,

	/**
	 * A segment without any movement.
	 */
	NO_MOVEMENT,

	/**
	 * A segment where the mob is removed.
	 */
	REMOVE_MOB,

	/**
	 * A segment with movement in two directions.
	 */
	RUN,

	/**
	 * A segment where the mob is teleported.
	 */
	TELEPORT,

	/**
	 * A segment with movement in a single direction.
	 */
	WALK;

}