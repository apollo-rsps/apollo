package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player adds someone to their friends list.
 *
 * @author Major
 */
public final class AddFriendMessage extends Message {

	/**
	 * The username of the befriended player.
	 */
	private final String username;

	/**
	 * Creates a new befriend user message.
	 *
	 * @param username The befriended player's username.
	 */
	public AddFriendMessage(String username) {
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