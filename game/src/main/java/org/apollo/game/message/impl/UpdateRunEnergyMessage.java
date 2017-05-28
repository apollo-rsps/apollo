package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to update the remaining run energy value.
 *
 * @author Major
 */
public final class UpdateRunEnergyMessage extends Message {

	/**
	 * The run energy.
	 */
	private final int energy;

	/**
	 * Creates a new update run energy message.
	 *
	 * @param energy The energy.
	 */
	public UpdateRunEnergyMessage(int energy) {
		this.energy = energy;
	}

	/**
	 * Gets the amount of run energy.
	 *
	 * @return The energy.
	 */
	public int getEnergy() {
		return energy;
	}

}