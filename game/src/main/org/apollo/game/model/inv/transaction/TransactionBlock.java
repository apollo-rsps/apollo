package org.apollo.game.model.inv.transaction;

/**
 * Represents a single transaction block in an {@link ItemTransaction}.
 */
public interface TransactionBlock {

	/**
	 * Attempts to commit changes made in this transaction.
	 * 
	 * @return {@code true} iff this transaction succeeds, otherwise {@code false}.
	 */
	boolean commit();

	/**
	 * Reverts this transaction back to its original state.
	 */
	void revert();

}