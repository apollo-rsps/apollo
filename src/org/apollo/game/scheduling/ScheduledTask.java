package org.apollo.game.scheduling;

/**
 * A game-related task that is scheduled to run in the future.
 * @author Graham
 */
public abstract class ScheduledTask {

	/**
	 * A flag indicating if the task is running.
	 */
	private boolean running = true;

	/**
	 * The delay between executions of the task, in pulses.
	 */
	private int delay;

	/**
	 * The number of pulses remaining until the task is next executed.
	 */
	private int pulses;

	/**
	 * Creates a new scheduled task.
	 * @param delay The delay between executions of the task, in pulses.
	 * @param immediate A flag indicating if this task should (for the first
	 * execution) be ran immediately, or after the {@code delay}.
	 * @throws IllegalArgumentException if the delay is less than or equal to
	 * zero.
	 */
	public ScheduledTask(int delay, boolean immediate) {
		setDelay(delay);
		this.pulses = immediate ? 0 : delay;
	}

	/**
	 * Checks if this task is running.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Sets the delay.
	 * @param delay The delay.
	 * @throws IllegalArgumentException if the delay is less than or equal to
	 * zero.
	 */
	public void setDelay(int delay) {
		if (delay < 0) {
			throw new IllegalArgumentException();
		}
		this.delay = delay;
	}

	/**
	 * Stops the task.
	 */
	public void stop() {
		running = false;
	}

	/**
	 * Pulses this task: updates the delay and calls {@link #execute()} if
	 * necessary.
	 */
	final void pulse() {
		if (running && pulses-- == 0) {
			execute();
			pulses = delay;
		}
	}

	/**
	 * Executes this task.
	 */
	public abstract void execute();

}
