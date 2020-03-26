package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed model.
 *
 * @author Major
 */
public final class IfSetModelMessage extends Message {

	/**
	 * The model id.
	 */
	private final int model;

	/**
	 * The interface id.
	 */
	private final int interfacePacked;

	/**
	 * Creates a new SetWidgetModelMessage.
	 *
	 * @param interfacePacked The interface id.
	 * @param model The model id.
	 */
	public IfSetModelMessage(int interfacePacked, int componentId, int model) {
		this.interfacePacked = interfacePacked << 16 | componentId;
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
	public int getInterfacePacked() {
		return interfacePacked;
	}

}