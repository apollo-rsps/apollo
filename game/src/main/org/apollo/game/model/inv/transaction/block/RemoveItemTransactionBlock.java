package org.apollo.game.model.inv.transaction.block;

import java.util.Optional;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.transaction.TransactionBlock;

public final class RemoveItemTransactionBlock implements TransactionBlock {

	private final Inventory inventory;

	private final Item item;

	private Optional<Item> removed = Optional.empty();

	public RemoveItemTransactionBlock(Inventory inventory, Item item) {
		this.inventory = inventory;
		this.item = item;
	}

	@Override
	public boolean commit() {
		int slot = inventory.slotOf(item.getId());
		removed = inventory.remove(slot);
		return removed.get().equals(item);
	}

	@Override
	public void revert() {
		removed.ifPresent(inventory::add);
		removed = Optional.empty();
	}

}