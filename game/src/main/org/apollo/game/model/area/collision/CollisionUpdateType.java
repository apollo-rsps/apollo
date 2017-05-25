package org.apollo.game.model.area.collision;

/**
 * An enum which represents the type of a {@link CollisionUpdate}.
 */
public enum CollisionUpdateType {

	/**
	 * Indicates that a {@link CollisionUpdate} will be adding new flags to collision matrices.
	 */
	ADDING,

	/**
	 * Indicates that a {@link CollisionUpdate} will be clearing existing flags from collision matrices.
	 */
	REMOVING

}
