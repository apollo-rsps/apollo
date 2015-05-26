package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's text.
 *
 * @author Graham
 */
public final class SetWidgetTextMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int interfaceId;

	/**
	 * The text.
	 */
	private final String text;

	/**
	 * Creates the set interface text message.
	 *
	 * @param interfaceId The interface's id.
	 * @param text The interface's text.
	 */
	public SetWidgetTextMessage(int interfaceId, String text) {
		this.interfaceId = interfaceId;
		this.text = text;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the interface's text.
	 *
	 * @return The interface's text.
	 */
	public String getText() {
		return text;
	}

}