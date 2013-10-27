package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.SlottedItem;

/**
 * An event which updates a single item in an interface.
 * @author Graham
 */
public final class UpdateSlottedItemsEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The slotted items.
	 */
	private final SlottedItem[] items;

	/**
	 * Creates the update item in interface event.
	 * @param interfaceId The interface id.
	 * @param items The slotted items.
	 */
	public UpdateSlottedItemsEvent(int interfaceId, SlottedItem... items) {
		this.interfaceId = interfaceId;
		this.items = items;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets an array of slotted items.
	 * @return The slotted items.
	 */
	public SlottedItem[] getSlottedItems() {
		return items;
	}

}
