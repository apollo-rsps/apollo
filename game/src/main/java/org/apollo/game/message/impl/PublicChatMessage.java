package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to send a public chat message to other players.
 *
 * @author Graham
 */
public final class PublicChatMessage extends ChatMessage {

	public enum PublicChatType {
		NONE, QUICKCHAT, CLANCHAT
	}

	/**
	 * The text color.
	 */
	private final int color;

	/**
	 * The text effects.
	 */
	private final int effects;

	/**
	 * The type of public chat.
	 */
	private final PublicChatType type;

	/**
	 * Creates a new chat message.
	 *
	 * @param message           The message.
	 * @param compressedMessage The compressed message.
	 * @param color             The text color.
	 * @param effects           The text effects.
	 * @param type              The chat type.
	 */
	public PublicChatMessage(String message, byte[] compressedMessage, int color, int effects, PublicChatType type) {
		super(message, compressedMessage);
		this.color = color;
		this.effects = effects;
		this.type = type;
	}

	/**
	 * Gets the text color.
	 *
	 * @return The text color.
	 */
	public int getTextColor() {
		return color;
	}

	/**
	 * Gets the text effects.
	 *
	 * @return The text effects.
	 */
	public int getTextEffects() {
		return effects;
	}

	/**
	 * Gets the chat type.
	 *
	 * @return the type of public chat.
	 */
	public PublicChatType getType() {
		return type;
	}
}