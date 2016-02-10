package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed model.
 *
 * @author Major
 */
public final class SetWidgetModelMessage extends Message {

	/**
	 * The model id.
	 */
	private final int model;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * Creates a new SetWidgetModelMessage.
	 *
	 * @param interfaceId The interface id.
	 * @param model The model id.
	 */
	public SetWidgetModelMessage(int interfaceId, int model) {
		this.interfaceId = interfaceId;
		this.model = model;
	}

	/**
	 * Gets the model id.
	 *
	 * @return The model id.
	 */
	public int getModel() {
		return model;
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