package org.apollo.game.model.inv.transaction;

public interface TransactionBlock {

	boolean commit();

	void revert();

}