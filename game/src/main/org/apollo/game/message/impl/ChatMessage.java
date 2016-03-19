package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to send a chat message to other players.
 *
 * @author AymericDu
 */
public abstract class ChatMessage extends Message {

	/**
	 * The message.
	 */
	private final String message;

	/**
	 * The compressed message.
	 */
	private final byte[] compressedMessage;

	/**
	 * Creates a new chat message.
	 *
	 * @param message The message.
	 * @param compressedMessage The compressed message.
	 */
	public ChatMessage(String message, byte[] compressedMessage) {
		this.message = message;
		this.compressedMessage = compressedMessage.clone();
	}

	/**
	 * Gets the compressed message.
	 *
	 * @return The compressed message.
	 */
	public final byte[] getCompressedMessage() {
		return compressedMessage.clone();
	}

	/**
	 * Gets the message.
	 *
	 * @return The message.
	 */
	public final String getMessage() {
		return message;
	}
}
