package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed item model.
 *
 * @author Khaled Abdeljaber
 */
public final class IfClearItemsMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int packedInterface;

	/**
	 * Creates a new set interface item model message.
	 *
	 * @param interfaceId The interface's id.
	 * @param componentId The component id.
	 */
	public IfClearItemsMessage(int interfaceId, int componentId) {
		this.packedInterface = interfaceId << 16 | componentId;
	}

	/**
	 * Gets the interface's id.
	 *
	 * @return The id.
	 */
	public int getPackedInterface() {
		return packedInterface;
	}
}