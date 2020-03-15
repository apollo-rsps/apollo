package org.apollo.game.message.impl;

import com.google.common.base.Preconditions;
import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to request that the player walks somewhere.
 *
 * @author Graham
 */
public final class WalkMessage extends Message {

	/**
	 * The running flag.
	 */
	private final boolean run;

	/**
	 * The steps.
	 */
	private final Position[] steps;

	/**
	 * Creates the message.
	 *
	 * @param steps The steps array.
	 * @param run The run flag.
	 */
	public WalkMessage(Position[] steps, boolean run) {
		Preconditions.checkArgument(steps.length >= 0, "Number of steps cannot be negative.");
		this.steps = steps;
		this.run = run;
	}

	/**
	 * Gets the steps array.
	 *
	 * @return An array of steps.
	 */
	public Position[] getSteps() {
		return steps;
	}

	/**
	 * Checks if the steps should be ran (ctrl+click).
	 *
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return run;
	}

}