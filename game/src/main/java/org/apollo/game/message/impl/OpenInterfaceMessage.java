package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that opens an interface.
 *
 * @author Graham
 */
public final class OpenInterfaceMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int id;

	/**
	 * Creates the message with the specified interface id.
	 *
	 * @param id The interface id.
	 */
	public OpenInterfaceMessage(int id) {
		this.id = id;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getId() {
		return id;
	}

}