package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * Represents a change in the client's focus (if it is the active window).
 * 
 * @author Major
 */
public class FocusUpdateEvent extends Event {

	/**
	 * Dictates whether the client is focused or not.
	 */
	private final boolean focused;

	/**
	 * Creates a new focus update event.
	 * 
	 * @param update The data received.
	 */
	public FocusUpdateEvent(boolean focused) {
		this.focused = focused;
	}

	/**
	 * Indicates whether or not the client is focused.
	 * 
	 * @return {@code true} if the client is focused, otherwise {@code false}.
	 */
	public boolean isFocused() {
		return focused;
	}

}