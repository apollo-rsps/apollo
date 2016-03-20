package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to send a public chat message to other players.
 *
 * @author Graham
 */
public final class PublicChatMessage extends ChatMessage {

	/**
	 * The text color.
	 */
	private final int color;

	/**
	 * The text effects.
	 */
	private final int effects;

	/**
	 * Creates a new chat message.
	 *
	 * @param message The message.
	 * @param compressedMessage The compressed message.
	 * @param color The text color.
	 * @param effects The text effects.
	 */
	public PublicChatMessage(String message, byte[] compressedMessage, int color, int effects) {
		super(message, compressedMessage);
		this.color = color;
		this.effects = effects;
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

}