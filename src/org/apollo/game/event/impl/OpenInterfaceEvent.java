package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which opens an interface.
 * @author Graham
 */
public final class OpenInterfaceEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int id;

	/**
	 * Creates the event with the specified interface id.
	 * @param id The interface id.
	 */
	public OpenInterfaceEvent(int id) {
		this.id = id;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getId() {
		return id;
	}

}
