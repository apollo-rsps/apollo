package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's text.
 *
 * @author Graham
 */
public final class IfSetTextMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int packedInterface;

	/**
	 * The text.
	 */
	private final String text;

	/**
	 * Creates the set interface text message.
	 *
	 * @param interfaceId The interface's id.
	 * @param componentId The component id.
	 * @param text        The interface's text.
	 */
	public IfSetTextMessage(int interfaceId, int componentId, String text) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.text = text;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getPackedInterface() {
		return packedInterface;
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