package org.apollo.game.model.area;

/**
 * An operation that can be performed by a region, used by {@link RegionListener}s to differentiate between operations.
 * 
 * @author Major
 */
public enum RegionOperation {

	/**
	 * The add operation, when an entity has been added to a region.
	 */
	ADD,

	/**
	 * The move operation, when an entity has moved positions, but is still in the same region.
	 */
	MOVE,

	/**
	 * The remove operation, when an entity has been removed from a region.
	 */
	REMOVE;

}