package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player removes someone from their friends list.
 *
 * @author Major
 */
public final class RemoveFriendMessage extends Message {

	/**
	 * The username of the defriended player.
	 */
	private final String username;

	/**
	 * Creates a new defriend user message.
	 *
	 * @param username The defriended player's username.
	 */
	public RemoveFriendMessage(String username) {
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