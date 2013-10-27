package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which is periodically sent by the client to keep a connection
 * alive.
 * @author Graham
 */
public final class KeepAliveEvent extends Event {

	/**
	 * The time this event was created.
	 */
	private final long createdAt;

	/**
	 * Creates the keep alive event.
	 */
	public KeepAliveEvent() {
		createdAt = System.currentTimeMillis();
	}

	/**
	 * Gets the time when this event was created.
	 * @return The time when this event was created.
	 */
	public long getCreatedAt() {
		return createdAt;
	}

}
