package org.apollo.game.model.entity.attr;

/**
 * The persistence state of an attribute - either {@code PERSISTENT} (saved) or {@code TRANSIENT} (not saved).
 */
public enum AttributePersistence {

	/**
	 * The serialized persistence type, indicating that the attribute will be saved.
	 */
	PERSISTENT,

	/**
	 * The transient persistence type, indicating that the attribute will not be saved.
	 */
	TRANSIENT;

}