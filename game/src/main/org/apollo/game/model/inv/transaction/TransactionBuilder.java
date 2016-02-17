package org.apollo.game.model.inv.transaction;

import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inv.Inventory;

public final class TransactionBuilder {

	private final ItemTransaction transaction = new ItemTransaction();
	private final Inventory inventory;

	private TransactionBuilder(Inventory inventory) {
		this.inventory = inventory;
		transaction.sync(inventory);
	}

	public TransactionBuilder failureMessage(Player player, String message) {
		return listener(new TransactionFailureListener(player, message));
	}

	public TransactionBuilder listener(TransactionListener listener) {
		transaction.setListener(listener);
		return this;
	}

	public TransactionBuilder swap(Inventory source, int fromSlot, int toSlot) {
		// TODO: Swap item block
		transaction.sync(source);
		return this;
	}

	public TransactionBuilder add(Item item) {
		// TODO: Add item block
		return this;
	}

	public TransactionBuilder remove(Item item) {
		// TODO: Remove item block
		return this;
	}

	public TransactionBuilder move(Inventory source, Item item) {
		// TODO: Move item block
		transaction.sync(source);
		return this;
	}

	public TransactionBuilder take(Inventory destination, Item item) {
		// TODO: Move item block
		transaction.sync(destination);
		return this;
	}

	public ItemTransaction build() {
		return transaction;
	}

	public static TransactionBuilder create(Inventory inventory) {
		return new TransactionBuilder(inventory);
	}

}