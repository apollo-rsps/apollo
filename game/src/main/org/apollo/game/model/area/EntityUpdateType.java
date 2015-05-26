package org.apollo.game.model.area;

/**
 * A type of update that an Entity in a {@link Region} may have.
 *
 * @author Major
 */
public enum EntityUpdateType {

	/**
	 * The add type, when an Entity has been added to a {@link Region}.
	 */
	ADD,

	/**
	 * The remove type, when an Entity has been removed from a {@link Region}.
	 */
	REMOVE;

}