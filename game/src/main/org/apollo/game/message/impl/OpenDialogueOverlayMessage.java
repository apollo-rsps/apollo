package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that opens a dialogue interface (an interface that appears in the chat box).
 *
 * @author Chris Fletcher
 */
public final class OpenDialogueOverlayMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new message with the specified interface id.
	 *
	 * @param interfaceId The interface id.
	 */
	public OpenDialogueOverlayMessage(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}