package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent to the client to display a server chat message.
 * 
 * @author Graham
 */
public final class ServerChatMessage extends Message {

	/**
	 * The chat message.
	 */
	private final String message;

	/**
	 * Creates a server chat message.
	 * 
	 * @param message The chat message.
	 */
	public ServerChatMessage(String message) {
		this(message, false);
	}

	/**
	 * Creates a server chat message.
	 * 
	 * @param message The chat message.
	 * @param filterable Whether or not the message can be filtered.
	 */
	public ServerChatMessage(String message, boolean filterable) {
		this.message = message + (filterable ? ":filterable:" : "");
	}

	/**
	 * Gets the chat message.
	 * 
	 * @return The chat message.
	 */
	public String getMessage() {
		return message;
	}

}