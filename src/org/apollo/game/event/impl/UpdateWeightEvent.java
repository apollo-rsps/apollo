package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to update the player's weight.
 * 
 * @author Major
 */
public final class UpdateWeightEvent extends Event {

	/**
	 * The weight of the player
	 */
	private final int weight;

	/**
	 * Creates the update weight event.
	 * 
	 * @param weight The weight of the player.
	 */
	public UpdateWeightEvent(int weight) {
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