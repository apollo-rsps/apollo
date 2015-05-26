package org.apollo.game.model.inter;

/**
 * Represents the different types of interfaces.
 *
 * @author Graham
 */
public enum InterfaceType {

	/**
	 * An interface that appears in the chat box.
	 */
	DIALOGUE,

	/**
	 * An interface shown behind a fullscreen window.
	 */
	FULLSCREEN_BACKGROUND,

	/**
	 * An interface shown in full screen mode.
	 */
	FULLSCREEN_WINDOW,

	/**
	 * An interface that occupies the game screen like a window, but the player can still perform actions without the
	 * interface closing.
	 */
	OVERLAY,

	/**
	 * An interface displayed over the inventory area.
	 */
	SIDEBAR,

	/**
	 * An interface that occupies the game screen.
	 */
	WINDOW;

}