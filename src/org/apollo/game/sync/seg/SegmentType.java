package org.apollo.game.sync.seg;

/**
 * An enumeration which contains the types of segments.
 * @author Graham
 */
public enum SegmentType {

	/**
	 * A segment without any movement.
	 */
	NO_MOVEMENT,

	/**
	 * A segment with movement in a single direction.
	 */
	WALK,

	/**
	 * A segment with movement in two directions.
	 */
	RUN,

	/**
	 * A segment where the character is teleported.
	 */
	TELEPORT,

	/**
	 * A segment where the character is added.
	 */
	ADD_CHARACTER,

	/**
	 * A segment where the character is removed.
	 */
	REMOVE_CHARACTER;

}
