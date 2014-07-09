package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when a player removes someone from their friends list.
 * 
 * @author Major
 */
public final class RemoveFriendEvent extends Event {

    /**
     * The username of the defriended player.
     */
    private final String username;

    /**
     * Creates a new defriend user event.
     * 
     * @param username The defriended player's username.
     */
    public RemoveFriendEvent(String username) {
	this.username = username;
    }

    /**
     * Gets the username of the defriended player.
     * 
     * @return The username.
     */
    public String getUsername() {
	return username;
    }

}