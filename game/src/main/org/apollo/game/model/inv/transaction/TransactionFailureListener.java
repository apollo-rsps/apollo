package org.apollo.game.model.inv.transaction;

import org.apollo.game.model.entity.Player;

/**
 * A TransactionListener which listens for failure events.
 */
public final class TransactionFailureListener implements TransactionListener {

	/**
	 * The Player.
	 */
	private final Player player;

	/**
	 * The message sent upon failure.
	 */
	private final String message;

	/**
	 * Creates a new {@link TransactionFailureListener}.
	 * 
	 * @param player The Player.
	 * @param message The message sent upon failure.
	 */
	public TransactionFailureListener(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	@Override
	public void alertFailure() {
		player.sendMessage(message);
	}

}