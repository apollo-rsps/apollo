package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} periodically sent by the client to keep a connection alive.
 *
 * @author Graham
 */
public final class KeepAliveMessage extends Message {

	/**
	 * The time this message was created.
	 */
	private final long createdAt;

	/**
	 * Creates the keep alive message.
	 */
	public KeepAliveMessage() {
		createdAt = System.currentTimeMillis();
	}

	/**
	 * Gets the time when this message was created.
	 *
	 * @return The time when this message was created.
	 */
	public long getCreatedAt() {
		return createdAt;
	}

}