package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to display a server chat message.
 *
 * @author Graham
 */
public final class ServerChatMessage extends Message {

	/**
	 * The message to send.
	 */
	private final String message;

	/**
	 * Creates the ServerChatMessage.
	 *
	 * @param message The chat message to send.
	 */
	public ServerChatMessage(String message) {
		this.message = message;
	}

	/**
	 * Gets the chat message to send.
	 *
	 * @return The chat message.
	 */
	public String getMessage() {
		return message;
	}

}