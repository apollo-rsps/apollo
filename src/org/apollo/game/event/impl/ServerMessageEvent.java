package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to display a server message.
 * 
 * @author Graham
 */
public final class ServerMessageEvent extends Event {

	/**
	 * The message.
	 */
	private final String message;

	/**
	 * Creates a server message event.
	 * 
	 * @param message The message.
	 */
	public ServerMessageEvent(String message) {
		this(message, false);
	}

	/**
	 * Creates a server message event.
	 * 
	 * @param message The message.
	 * @param filterable If the message can be filtered.
	 */
	public ServerMessageEvent(String message, boolean filterable) {
		this.message = message + (filterable ? ":filterable:" : "");
	}

	/**
	 * Gets the message.
	 * 
	 * @return The message.
	 */
	public String getMessage() {
		return message;
	}

}