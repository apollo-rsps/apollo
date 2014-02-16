package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when the player clicks the "Click here to continue" button on a dialogue
 * interface.
 * 
 * @author Chris Fletcher
 */
public final class DialogueContinueEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new dialogue continue event.
	 * 
	 * @param interfaceId The interface id.
	 */
	public DialogueContinueEvent(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id of the button.
	 * 
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}