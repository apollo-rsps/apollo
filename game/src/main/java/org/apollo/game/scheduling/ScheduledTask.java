package org.apollo.game.scheduling;

import com.google.common.base.Preconditions;

/**
 * A game-related task that is scheduled to run in the future.
 *
 * @author Graham
 */
public abstract class ScheduledTask {

	/**
	 * The delay between executions of the task, in pulses.
	 */
	private int delay;

	/**
	 * The number of pulses remaining until the task is next executed.
	 */
	private int pulses;

	/**
	 * A flag indicating if the task is running.
	 */
	private boolean running = true;

	/**
	 * Creates a new scheduled task.
	 *
	 * @param delay The delay between executions of the task, in pulses.
	 * @param immediate Indicates whether or not this task should be executed immediately, or after the {@code delay}.
	 * @throws IllegalArgumentException If the delay is less than or equal to zero.
	 */
	public ScheduledTask(int delay, boolean immediate) {
		setDelay(delay);
		pulses = immediate ? 0 : delay;
	}

	/**
	 * Checks if this task is running.
	 *
	 * @return {@code true} if so, {@code false} if not.
	 */
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Sets the delay.
	 *
	 * @param delay The delay.
	 * @throws IllegalArgumentException If the delay is less than zero.
	 */
	public final void setDelay(int delay) {
		Preconditions.checkArgument(delay >= 0, "Delay cannot be less than 0.");
		this.delay = delay;
	}

	/**
	 * Stops the task.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Executes this task.
	 */
	public abstract void execute();

	/**
	 * Pulses this task: updates the delay and calls {@link #execute()} if necessary.
	 */
	public final void pulse() {
		if (running && --pulses <= 0) {
			execute();
			pulses = delay;
		}
	}

}