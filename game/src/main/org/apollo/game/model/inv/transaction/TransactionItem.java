package org.apollo.game.model.inv.transaction;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;

public abstract class TransactionItem {

	protected final Inventory inventory;

	protected TransactionItem(Inventory inventory) {
		this.inventory = inventory;
	}

	public abstract Item delete();

	public abstract boolean deleteSuccessful();

	public abstract void revert();

}