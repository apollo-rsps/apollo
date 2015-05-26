package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player removes someone from their ignore list.
 *
 * @author Major
 */
public final class RemoveIgnoreMessage extends Message {

	/**
	 * The username of the unignored player.
	 */
	private final String username;

	/**
	 * Creates a new unignore player message.
	 *
	 * @param username The unignored player's username.
	 */
	public RemoveIgnoreMessage(String username) {
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