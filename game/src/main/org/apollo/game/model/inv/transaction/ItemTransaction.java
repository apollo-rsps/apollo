package org.apollo.game.model.inv.transaction;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import org.apollo.game.model.Item;
import org.apollo.game.model.inv.Inventory;

/**
 * Represents a transaction of {@link Item}s.
 */
public final class ItemTransaction {

	/**
	 * A {@link Queue} of TransactionBlocks not yet committed.
	 */
	private final Queue<TransactionBlock> queuedBlocks = new ArrayDeque<>();

	/**
	 * A {@link List} of TransactionBlocks that were successfully committed.
	 */
	private final List<TransactionBlock> committedBlocks = new ArrayList<>();

	/**
	 * A {@link Set} of {@link Inventory}s that were synchronized during this ItemTransaction.
	 */
	private final Set<Inventory> inventories = new HashSet<>();

	/**
	 * This transactions listener, wrapped in an {@link Optional}.
	 */
	private Optional<TransactionListener> listener = Optional.empty();

	/**
	 * Sets this ItemTransactions {@link TransactionListener}.
	 *
	 * @param listener The TransactionListener to set.
	 */
	public void setListener(TransactionListener listener) {
		this.listener = Optional.of(listener);
	}

	/**
	 * Synchronizes the specified Inventory with this ItemTransaction.
	 * 
	 * @param inventory The Inventory to synchronize.
	 */
	public void sync(Inventory inventory) {
		inventories.add(inventory);
	}

	/**
	 * Queues the specified TransactionBlock to be committed.
	 *
	 * @param block The TransactionBlock to commit.
	 */
	public void append(TransactionBlock block) {
		queuedBlocks.add(block);
	}

	/**
	 * Refreshes all of the synchronized inventories.
	 */
	public void refresh() {
		inventories.forEach(Inventory::forceRefresh);
	}

	/**
	 * Reverts all of the committed TransactionBlocks and notifies failure.
	 */
	public void rollback() {
		committedBlocks.forEach(TransactionBlock::revert);
		listener.ifPresent(TransactionListener::alertFailure);
	}

	/**
	 * Attempts to commit all of the {@link #queuedBlocks queued TransactionBlocks}.
	 * 
	 * @return {@code true} if we were able to successfully commit all of the
	 *         TransactionBlocks, otherwise {@code false} and all previously
	 *         committed TransactionBlocks are reverted back to their original
	 *         state.
	 */
	public boolean commit() {
		for (TransactionBlock block : queuedBlocks) {
			if (!block.commit()) {
				rollback();
				return false;
			}
			committedBlocks.add(block);
		}
		refresh();
		return true;
	}

}