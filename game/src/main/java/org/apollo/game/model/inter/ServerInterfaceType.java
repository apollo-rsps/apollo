package org.apollo.game.model.inter;

/**
 * Represents the different types of interfaces.
 *
 * @author Graham
 */
public enum ServerInterfaceType {

	/**
	 * These are actually sent to the client.
	 */
	MODAL,
	OVERLAY,

	/**
	 * These are organizational.
	 */
	TOPLEVEL,
	TOP,
	DIALOGUE
}