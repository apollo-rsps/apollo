package org.apollo.game.model.inv.transaction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apollo.game.model.inv.Inventory;

public final class ItemTransaction {

	private final Queue<TransactionBlock> queuedBlocks = new ArrayDeque<>();
	private final List<TransactionBlock> committedBlocks = new ArrayList<>();
	private final Set<Inventory> inventories = new HashSet<>();
	private TransactionListener listener;

	public void setListener(TransactionListener listener) {
		this.listener = listener;
	}

	public void sync(Inventory inventory) {
		inventories.add(inventory);
	}

	public void append(TransactionBlock block) {
		queuedBlocks.add(block);
	}

	public void refresh() {
		inventories.forEach(Inventory::forceRefresh);
	}

	public void rollback() {
		committedBlocks.forEach(TransactionBlock::revert);
		listener.alertFailure();
	}

	public boolean commit() {
		for (TransactionBlock change : queuedBlocks) {
			if (!change.commit()) {
				rollback();
				return false;
			}
			committedBlocks.add(change);
		}
		refresh();
		return true;
	}

}