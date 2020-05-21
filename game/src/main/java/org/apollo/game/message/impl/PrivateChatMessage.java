package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to send private chat to another player.
 *
 * @author Major
 */
public final class PrivateChatMessage extends ChatMessage {

	/**
	 * The username this message is being sent to.
	 */
	private final String username;

	/**
	 * Creates a new private chat message.
	 *
	 * @param message The chat string.
	 * @param compressedMessage The chat string, in a compressed form.
	 * @param username The username of the player the message is being sent to.
	 */
	public PrivateChatMessage(String message, byte[] compressedMessage, String username) {
		super(message, compressedMessage);
		this.username = username;
	}

	/**
	 * Gets the username of the player the chat string is being sent to.
	 *
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

}