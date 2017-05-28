package org.apollo.game.message.impl;

import org.apollo.game.model.Item;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that updates all the items in an interface.
 *
 * @author Graham
 */
public final class UpdateItemsMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The items.
	 */
	private final Item[] items;

	/**
	 * Creates the update inventory interface message.
	 *
	 * @param interfaceId The interface id.
	 * @param items The items.
	 */
	public UpdateItemsMessage(int interfaceId, Item[] items) {
		this.interfaceId = interfaceId;
		this.items = items;
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
	 * Gets the items.
	 *
	 * @return The items.
	 */
	public Item[] getItems() {
		return items;
	}

}