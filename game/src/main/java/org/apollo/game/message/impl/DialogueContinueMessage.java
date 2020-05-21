package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when the player clicks the "Click here to continue" button on a dialogue
 * interface.
 *
 * @author Chris Fletcher
 */
public final class DialogueContinueMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new dialogue continue message.
	 *
	 * @param interfaceId The interface id.
	 */
	public DialogueContinueMessage(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface id of the button.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}