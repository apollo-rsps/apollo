package org.apollo.game.model;

/**
 * Represents the different types of interfaces.
 * 
 * @author Graham
 */
public enum InterfaceType {

	/**
	 * A dialogue is an interface which appears in the chat box.
	 */
	DIALOGUE,

	/**
	 * An interface which is shown behind a fullscreen window.
	 */
	FULLSCREEN_BACKGROUND,

	/**
	 * An interface which is shown in full screen mode.
	 */
	FULLSCREEN_WINDOW,

	/**
	 * An overlay is an interface which occupies the game screen like a window, however, you can walk around and perform
	 * actions still.
	 */
	OVERLAY,

	/**
	 * An interface which displays over the inventory area.
	 */
	SIDEBAR,

	/**
	 * A window is an interface which occupies the game screen.
	 */
	WINDOW;

}