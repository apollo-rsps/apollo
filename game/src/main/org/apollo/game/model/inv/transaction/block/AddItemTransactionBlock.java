package org.apollo.game.model.inv.transaction.block;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.transaction.TransactionBlock;

/**
 * A TransactionBlock which adds an Item to an Inventory.
 */
public final class AddItemTransactionBlock implements TransactionBlock {

	/**
	 * The Inventory the Item is being added to.
	 */
	private final Inventory inventory;

	/**
	 * The Item we are adding to the Inventory.
	 */
	private final Item item;

	/**
	 * Creates a new {@link AddItemTransactionBlock}.
	 *
	 * @param inventory The Inventory we are adding the specified Item to.
	 * @param item The Item to add to the Inventory.
	 */
	public AddItemTransactionBlock(Inventory inventory, Item item) {
		this.inventory = inventory;
		this.item = item;
	}

	@Override
	public boolean commit() {
		return !inventory.add(item).isPresent();
	}

	@Override
	public void revert() {
		inventory.remove(item);
	}

}