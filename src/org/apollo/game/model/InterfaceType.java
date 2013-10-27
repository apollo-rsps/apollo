package org.apollo.game.model;

/**
 * Represents the different types of interfaces.
 * @author Graham
 */
public enum InterfaceType {

	/**
	 * A window is an interface which occupies the game screen.
	 */
	WINDOW,

	/**
	 * An overlay is an interface which occupies the game screen like a window,
	 * however, you can walk around and perform actions still.
	 */
	OVERLAY,

	/**
	 * A dialogue is an interface which appears in the chat box.
	 */
	DIALOGUE,

	/**
	 * An interface which displays over the inventory area.
	 */
	SIDEBAR,

	/**
	 * An interface which is shown in full screen mode.
	 */
	FULLSCREEN_WINDOW,

	/**
	 * An interface which is shown behind a fullscreen window.
	 */
	FULLSCREEN_BACKGROUND;

}
