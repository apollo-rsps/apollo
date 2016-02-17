package org.apollo.game.model.inv.transaction;

import java.util.Optional;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;

/**
 * A TransactionItem that lies within a slot in some Inventory.
 */
public final class SlottedTransactionItem extends TransactionItem {

	/**
	 * The slot of the Item.
	 */
	private final int slot;

	/**
	 * The Item.
	 */
	private Optional<Item> removedItem = Optional.empty();

	/**
	 * Creates a new {@link SlottedTransactionItem}.
	 * 
	 * @param inventory The Inventory this TransactionItem is within.
	 * @param slot The slot of the Item.
	 */
	public SlottedTransactionItem(Inventory inventory, int slot) {
		super(inventory);
		this.slot = slot;
	}

	@Override
	public boolean deleteSuccessful() {
		return removedItem.isPresent();
	}

	/**
	 * Updates the Item in the specified {@code slot} with the specified Item.
	 * 
	 * @param item The new Item.
	 * @return The old Item.
	 */
	public Item update(Item item) {
		Item removed = inventory.set(slot, item);
		return removed;
	}

	@Override
	public Item delete() {
		if (removedItem.isPresent()) {
			throw new IllegalStateException("Item already deleted.");
		}
		removedItem = inventory.remove(slot);
		return removedItem.get();
	}

	@Override
	public void revert() {
		if (!removedItem.isPresent()) {
			throw new IllegalStateException("No changes to revert.");
		}
		inventory.set(slot, removedItem.get());
		removedItem = Optional.empty();
	}

}