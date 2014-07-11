package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when a player removes someone from their ignore list.
 * 
 * @author Major
 */
public final class RemoveIgnoreEvent extends Event {

	/**
	 * The username of the unignored player.
	 */
	private final String username;

	/**
	 * Creates a new unignore player event.
	 * 
	 * @param username The unignored player's username.
	 */
	public RemoveIgnoreEvent(String username) {
		this.username = username;
	}

	/**
	 * Gets the username of the unignored player.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

}