package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.entity.setting.ServerStatus;

/**
 * An {@link Event} sent to the client to update the friend server status.
 * 
 * @author Major
 */
public final class FriendServerStatusEvent extends Event {

    /**
     * The status code of the friend server.
     */
    private final int status;

    /**
     * Creates a new friend server status event.
     * 
     * @param status The status.
     */
    public FriendServerStatusEvent(ServerStatus status) {
	this.status = status.getCode();
    }

    /**
     * Gets the status code of the friend server.
     * 
     * @return The status code.
     */
    public int getStatusCode() {
	return status;
    }

}