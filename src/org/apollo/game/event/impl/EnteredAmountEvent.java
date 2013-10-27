package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent by the client when the player has entered an amount.
 * @author Graham
 */
public final class EnteredAmountEvent extends Event {

	/**
	 * The amount.
	 */
	private final int amount;

	/**
	 * Creates the entered amount event.
	 * @param amount The amount.
	 */
	public EnteredAmountEvent(int amount) {
		this.amount = amount;
	}

	/**
	 * Gets the amount.
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
	}

}
