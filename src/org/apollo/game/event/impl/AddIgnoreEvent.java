package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client when a player adds someone to their ignore list.
 * 
 * @author Major
 */
public final class AddIgnoreEvent extends Event {

	/**
	 * The username of the ignored player.
	 */
	private final String username;

	/**
	 * Creates a new ignore player event.
	 * 
	 * @param username The ignored player's username.
	 */
	public AddIgnoreEvent(String username) {
		this.username = username;
	}

	/**
	 * Gets the username of the ignored player.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

}