package org.apollo.game.io.player;

import org.apollo.game.model.inv.SlottedItem;

/**
 * A temporary-and intermediary data access object that holds information
 * about an item inside a specific inventory's slot, to insert into a player's
 * inventory.
 * @author Sino
 */
final class SlottedInventoryItem {
	private final int inventoryId;

	private final SlottedItem item;

	SlottedInventoryItem(int inventoryId, SlottedItem item) {
		this.inventoryId = inventoryId;
		this.item = item;
	}

	int getInventoryId() {
		return inventoryId;
	}

	SlottedItem getItem() {
		return item;
	}

	@Override
	public String toString() {
		return "SlottedInventoryItem{inventoryId=" + inventoryId + ", item=" + item + "}";
	}
}
