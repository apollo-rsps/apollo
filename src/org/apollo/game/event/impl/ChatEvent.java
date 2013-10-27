package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent by the client to send a public chat message to other players.
 * @author Graham
 */
public final class ChatEvent extends Event {

	/**
	 * The message.
	 */
	private final String message;

	/**
	 * The compressed message.
	 */
	private final byte[] compressedMessage;

	/**
	 * The text color.
	 */
	private final int color;

	/**
	 * The text effects.
	 */
	private final int effects;

	/**
	 * Creates a new chat event.
	 * @param message The message.
	 * @param compressedMessage The compressed message.
	 * @param color The text color.
	 * @param effects The text effects.
	 */
	public ChatEvent(String message, byte[] compressedMessage, int color, int effects) {
		this.message = message;
		this.compressedMessage = compressedMessage;
		this.color = color;
		this.effects = effects;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets the text color.
	 * @return The text color.
	 */
	public int getTextColor() {
		return color;
	}

	/**
	 * Gets the text effects.
	 * @return The text effects.
	 */
	public int getTextEffects() {
		return effects;
	}

	/**
	 * Gets the compressed message.
	 * @return The compressed message.
	 */
	public byte[] getCompressedMessage() {
		return compressedMessage;
	}

}
