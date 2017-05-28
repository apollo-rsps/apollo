package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client after a short period of time containing random data.
 *
 * @author Major
 */
public final class SpamPacketMessage extends Message {

	/**
	 * Data sent by the spam packet.
	 */
	private final byte[] data;

	/**
	 * Creates a new spam packet message.
	 *
	 * @param data The data sent.
	 */
	public SpamPacketMessage(byte[] data) {
		this.data = data;
	}

	/**
	 * Gets the data sent.
	 *
	 * @return The data.
	 */
	public byte[] getData() {
		return data;
	}

}