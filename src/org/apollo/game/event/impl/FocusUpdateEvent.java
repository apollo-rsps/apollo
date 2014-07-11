package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client to indicate a change in the client's focus (i.e. if it is the active window).
 * 
 * @author Major
 */
public final class FocusUpdateEvent extends Event {

	/**
	 * Indicates whether the client is focused or not.
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