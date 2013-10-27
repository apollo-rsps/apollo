package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which is sent to the client with a server-side message.
 * @author Graham
 */
public final class ServerMessageEvent extends Event {

	/**
	 * The message.
	 */
	private final String message;

	/**
	 * Creates the {@link ServerMessageEvent}.
	 * @param message The message.
	 */
	public ServerMessageEvent(String message) {
		this.message = message;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

}
