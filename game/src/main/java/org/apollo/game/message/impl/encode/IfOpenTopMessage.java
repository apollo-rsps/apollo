package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

public class IfOpenTopMessage extends Message {

	private final int id;

	/**
	 * Instantiates a new If open top message.
	 *
	 * @param id the interface id
	 */
	public IfOpenTopMessage(int id) {
		this.id = id;
	}

	/**
	 * Gets interface id.
	 *
	 * @return the interface id.
	 */
	public int getId() {
		return id;
	}
}
