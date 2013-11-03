package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent to the client to set an interface's player model.
 * 
 * @author Chris Fletcher
 */
public final class SetWidgetPlayerModelEvent extends Event {

	/**
	 * The interface's id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new set interface player model event.
	 * 
	 * @param interfaceId The interface's id.
	 */
	public SetWidgetPlayerModelEvent(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface's id.
	 * 
	 * @return The id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}
