package org.apollo.game.model.inv.transaction;

import org.apollo.game.model.entity.Player;

public final class TransactionFailureListener implements TransactionListener {

	private final Player player;
	private final String message;

	public TransactionFailureListener(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	@Override
	public void alertFailure() {
		player.sendMessage(message);
	}

}