package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to update the remaining run energy value.
 * 
 * @author Major
 */
public final class UpdateRunEnergyEvent extends Event {

    /**
     * The run energy.
     */
    private final int energy;

    /**
     * Creates a new update run energy event.
     * 
     * @param energy The energy.
     */
    public UpdateRunEnergyEvent(int energy) {
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