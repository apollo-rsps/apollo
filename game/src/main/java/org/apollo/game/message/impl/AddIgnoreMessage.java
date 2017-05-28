package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player adds someone to their ignore list.
 *
 * @author Major
 */
public final class AddIgnoreMessage extends Message {

	/**
	 * The username of the ignored player.
	 */
	private final String username;

	/**
	 * Creates a new ignore player message.
	 *
	 * @param username The ignored player's username.
	 */
	public AddIgnoreMessage(String username) {
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