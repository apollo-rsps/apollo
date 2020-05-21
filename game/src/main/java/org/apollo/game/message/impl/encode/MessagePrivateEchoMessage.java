package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type Message private message.
 *
 * @author Khaled Abdeljaber
 */
public class MessagePrivateEchoMessage extends Message {

	/**
	 * The username of the player sending the message.
	 */
	private final String username;

	/**
	 * The message.
	 */
	private final byte[] message;

	/**
	 * Creates a new forward private message message.
	 *
	 * @param username The username of the player sending the message.
	 * @param message  The compressed message.
	 */
	public MessagePrivateEchoMessage(String username, byte[] message) {
		this.username = username;
		this.message = message;
	}

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Get message byte [ ].
	 *
	 * @return the byte [ ]
	 */
	public byte[] getMessage() {
		return message;
	}
}
