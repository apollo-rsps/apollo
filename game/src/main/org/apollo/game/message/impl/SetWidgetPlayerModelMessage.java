package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed player model.
 *
 * @author Chris Fletcher
 */
public final class SetWidgetPlayerModelMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new set interface player model message.
	 *
	 * @param interfaceId The interface's id.
	 */
	public SetWidgetPlayerModelMessage(int interfaceId) {
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the interface's id.
	 *
	 * @return The id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}