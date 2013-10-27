package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Item;

/**
 * An event which updates all the items in an interface.
 * @author Graham
 */
public final class UpdateItemsEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The items.
	 */
	private final Item[] items;

	/**
	 * Creates the update inventory interface event.
	 * @param interfaceId The interface id.
	 * @param items The items.
	 */
	public UpdateItemsEvent(int interfaceId, Item[] items) {
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
	 * Gets the items.
	 * @return The items.
	 */
	public Item[] getItems() {
		return items;
	}

}
