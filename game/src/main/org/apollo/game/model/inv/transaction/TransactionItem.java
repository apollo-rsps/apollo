package org.apollo.game.model.inv.transaction;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;

/**
 * Represents an Item during an ItemTransaction.
 */
public abstract class TransactionItem {

	/**
	 * The Inventory this TransactionItem is within.
	 */
	protected final Inventory inventory;

	/**
	 * Creates a new {@link TransactionItem}.
	 *
	 * @param inventory The Inventory this TransactionItem is within.
	 */
	protected TransactionItem(Inventory inventory) {
		this.inventory = inventory;
	}

	/**
	 * Performs a transaction which moves, adds, swaps or deletes an Item from
	 * the Inventory.
	 * 
	 * @return The Item after the ItemTransaction has occurred.
	 */
	public abstract Item delete(); // TODO: Better name

	/**
	 * Tests whether or not the transaction was successful.
	 * 
	 * @return {@code true} if this ItemTransaction was successful, otherwise
	 *         {@code false}.
	 */
	public abstract boolean deleteSuccessful(); // TODO: Better name

	/**
	 * Reverts this ItemTransaction back to its original state.
	 */
	public abstract void revert();

}