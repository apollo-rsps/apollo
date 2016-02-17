package org.apollo.game.model.inv.transaction;

import java.util.Optional;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;

/**
 * A TransactionItem that is stacked within an Inventory.
 */
public final class StackedTransactionItem extends TransactionItem {

	/**
	 * The stacked Item.
	 */
	private final Item item;

	/**
	 * The removed Item.
	 */
	private Optional<Item> removedItem = Optional.empty();

	/**
	 * Creates a new {@link StackedTransactionItem}.
	 *
	 * @param inventory The Inventory this TransactionItem is within.
	 * @param item The stacked Item.
	 */
	public StackedTransactionItem(Inventory inventory, Item item) {
		super(inventory);
		this.item = item;
	}

	@Override
	public boolean deleteSuccessful() {
		if (!removedItem.isPresent()) {
			return false;
		}
		return removedItem.get().equals(item);
	}

	@Override
	public Item delete() {
		if (removedItem.isPresent()) {
			throw new IllegalStateException("Item already deleted.");
		}
		int slot = inventory.slotOf(item.getId());
		removedItem = inventory.remove(slot);
		return removedItem.get();
	}

	@Override
	public void revert() {
		if (!removedItem.isPresent()) {
			throw new IllegalStateException("No changes to revert.");
		}
		inventory.add(removedItem.get());
		removedItem = Optional.empty();
	}

}