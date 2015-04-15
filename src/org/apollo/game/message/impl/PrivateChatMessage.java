package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent by the client to send private chat to another player.
 *
 * @author Major
 */
public final class PrivateChatMessage extends Message {

	/**
	 * The chat string being sent.
	 */
	private final String chat;

	/**
	 * The compressed chat string.
	 */
	private final byte[] compressedChat;

	/**
	 * The username this message is being sent to.
	 */
	private final String username;

	/**
	 * Creates a new private chat message.
	 *
	 * @param username The username of the player the message is being sent to.
	 * @param chat The chat string.
	 * @param compressedChat The chat string, in a compressed form.
	 */
	public PrivateChatMessage(String username, String chat, byte[] compressedChat) {
		this.username = username;
		this.chat = chat;
		this.compressedChat = compressedChat.clone();
	}

	/**
	 * Gets the chat string being sent.
	 *
	 * @return The chat string.
	 */
	public String getChat() {
		return chat;
	}

	/**
	 * Gets the compressed chat string.
	 *
	 * @return The compressed chat string.
	 */
	public byte[] getCompressedChat() {
		return compressedChat.clone();
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