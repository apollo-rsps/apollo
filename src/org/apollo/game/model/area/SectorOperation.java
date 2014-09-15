package org.apollo.game.model.area;

/**
 * An operation that can be performed by a sector, used by {@link SectorListener}s to differentiate between operations.
 * 
 * @author Major
 */
public enum SectorOperation {

	/**
	 * The add operation, when an entity has been added to a sector.
	 */
	ADD,

	/**
	 * The remove operation, when an entity has been removed from a sector.
	 */
	REMOVE;

}