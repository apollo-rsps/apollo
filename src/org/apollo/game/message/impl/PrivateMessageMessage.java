package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent by the client to send a private message to another player.
 * 
 * @author Major
 */
public final class PrivateMessageMessage extends Message {

	/**
	 * The username this message is being sent to.
	 */
	private final String username;

	/**
	 * The message being sent.
	 */
	private final String message;

	/**
	 * The compressed message.
	 */
	private final byte[] compressedMessage;

	/**
	 * Creates a new private message message.
	 * 
	 * @param username The username of the player the message is being sent to.
	 * @param message The message.
	 * @param compressedMessage The message, in a compressed form.
	 */
	public PrivateMessageMessage(String username, String message, byte[] compressedMessage) {
		this.username = username;
		this.message = message;
		this.compressedMessage = compressedMessage;
	}

	/**
	 * Gets the username of the player the message is being sent to.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the message being sent.
	 * 
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the compressed message.
	 * 
	 * @return The compressed message.
	 */
	public byte[] getCompressedMessage() {
		return compressedMessage;
	}

}