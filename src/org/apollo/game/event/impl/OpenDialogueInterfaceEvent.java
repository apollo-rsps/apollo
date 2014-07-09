package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client that opens a dialogue interface (an interface that appears in the chat box).
 * 
 * @author Chris Fletcher
 */
public final class OpenDialogueInterfaceEvent extends Event {

    /**
     * The interface id.
     */
    private final int interfaceId;

    /**
     * Creates a new event with the specified interface id.
     * 
     * @param interfaceId The interface id.
     */
    public OpenDialogueInterfaceEvent(int interfaceId) {
	this.interfaceId = interfaceId;
    }

    /**
     * Gets the interface id.
     * 
     * @return The interface id.
     */
    public int getInterfaceId() {
	return interfaceId;
    }

}