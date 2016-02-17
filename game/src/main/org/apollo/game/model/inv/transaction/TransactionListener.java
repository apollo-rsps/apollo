package org.apollo.game.model.inv.transaction;

/**
 * Listens for events from an ItemTransaction.
 */
public interface TransactionListener {

	/**
	 * Called when an ItemTransaction has failed.
	 */
	void alertFailure();

}