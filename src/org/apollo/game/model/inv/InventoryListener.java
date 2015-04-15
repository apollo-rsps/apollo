package org.apollo.game.model.inv;

import org.apollo.game.model.Item;

/**
 * An interface which listens to events from an {@link Inventory}.
 *
 * @author Graham
 */
public interface InventoryListener {

	/**
	 * Called when the capacity of an inventory has been exceeded.
	 *
	 * @param inventory The inventory.
	 */
	public void capacityExceeded(Inventory inventory);

	/**
	 * Called when items have been updated in bulk.
	 *
	 * @param inventory The inventory.
	 */
	public void itemsUpdated(Inventory inventory);

	/**
	 * Called when an item has been updated.
	 *
	 * @param inventory The inventory.
	 * @param slot The slot.
	 * @param item The new item, or {@code null} if there is no new item.
	 */
	public void itemUpdated(Inventory inventory, int slot, Item item);

}