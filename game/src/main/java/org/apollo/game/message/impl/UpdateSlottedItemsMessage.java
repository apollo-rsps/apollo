package org.apollo.game.message.impl;

import org.apollo.game.model.inv.SlottedItem;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that updates a single item in an interface.
 *
 * @author Graham
 */
public final class UpdateSlottedItemsMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The slotted items.
	 */
	private final SlottedItem[] items;

	/**
	 * Creates the update item in interface message.
	 *
	 * @param interfaceId The interface id.
	 * @param items The slotted items.
	 */
	public UpdateSlottedItemsMessage(int interfaceId, SlottedItem... items) {
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
	 * Gets an array of slotted items.
	 *
	 * @return The slotted items.
	 */
	public SlottedItem[] getSlottedItems() {
		return items;
	}

}