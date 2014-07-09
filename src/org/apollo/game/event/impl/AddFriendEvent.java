package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when a player adds someone to their friends list.
 * 
 * @author Major
 */
public final class AddFriendEvent extends Event {

    /**
     * The username of the befriended player.
     */
    private final String username;

    /**
     * Creates a new befriend user event.
     * 
     * @param username The befriended player's username.
     */
    public AddFriendEvent(String username) {
	this.username = username;
    }

    /**
     * Gets the username of the befriended player.
     * 
     * @return The username.
     */
    public String getUsername() {
	return username;
    }

}