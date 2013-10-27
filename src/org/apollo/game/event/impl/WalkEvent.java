package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An event which the client sends to request that the player walks somewhere.
 * @author Graham
 */
public final class WalkEvent extends Event {

	/**
	 * The steps.
	 */
	private final Position[] steps;

	/**
	 * The running flag.
	 */
	private boolean run;

	/**
	 * Creates the event.
	 * @param steps The steps array.
	 * @param run The run flag.
	 */
	public WalkEvent(Position[] steps, boolean run) {
		if (steps.length < 0) {
			throw new IllegalArgumentException("number of steps must not be negative");
		}
		this.steps = steps;
		this.run = run;
	}

	/**
	 * Gets the steps array.
	 * @return An array of steps.
	 */
	public Position[] getSteps() {
		return steps;
	}

	/**
	 * Checks if the steps should be ran (ctrl+click).
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return run;
	}

}
