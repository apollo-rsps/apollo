package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent when the client clicks a button.
 * @author Graham
 */
public final class ButtonEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates the button event.
	 * @param interfaceId The interface id.
	 */
	public ButtonEvent(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}
