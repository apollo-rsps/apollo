package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to update the player's weight.
 *
 * @author Major
 */
public final class UpdateWeightMessage extends Message {

	/**
	 * The weight of the player
	 */
	private final int weight;

	/**
	 * Creates the update weight message.
	 *
	 * @param weight The weight of the player.
	 */
	public UpdateWeightMessage(int weight) {
		this.weight = weight;
	}

	/**
	 * Gets the weight of the player.
	 *
	 * @return The weight.
	 */
	public int getWeight() {
		return weight;
	}

}