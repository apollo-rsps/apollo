package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to set a widget's displayed item model.
 *
 * @author Khaled Abdeljaber
 */
public final class IfSetItemMessage extends Message {

	/**
	 * The interface's id.
	 */
	private final int packedInterface;

	/**
	 * The item id.
	 */
	private final int item;

	/**
	 * The zoom level.
	 */
	private final int amount;

	/**
	 * Creates a new set interface item model message.
	 *
	 * @param interfaceId The interface's id.
	 * @param componentId The component id.
	 * @param item The model's (item) id.
	 * @param amount The zoom level.
	 */
	public IfSetItemMessage(int interfaceId, int componentId, int item, int amount) {
		this.packedInterface = interfaceId << 16 | componentId;
		this.item = item;
		this.amount = amount;
	}

	/**
	 * Gets the interface's id.
	 *
	 * @return The id.
	 */
	public int getPackedInterface() {
		return packedInterface;
	}

	/**
	 * Gets the model's (item) id.
	 *
	 * @return The id.
	 */
	public int getItem() {
		return item;
	}

	/**
	 * Gets the zoom level.
	 *
	 * @return The zoom.
	 */
	public int getAmount() {
		return amount;
	}

}