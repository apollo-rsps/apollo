package org.apollo.game.scheduling;

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
	 * @param immediate A flag indicating if this task should (for the first execution) be ran immediately, or after the
	 *            {@code delay}.
	 * @throws IllegalArgumentException If the delay is less than or equal to zero.
	 */
	public ScheduledTask(int delay, boolean immediate) {
		setDelay(delay);
		pulses = immediate ? 0 : delay;
	}

	/**
	 * Executes this task.
	 */
	public abstract void execute();

	/**
	 * Checks if this task is running.
	 * 
	 * @return {@code true} if so, {@code false} if not.
	 */
	public final boolean isRunning() {
		return running;
	}

	/**
	 * Pulses this task: updates the delay and calls {@link #execute()} if necessary.
	 */
	final void pulse() {
		if (running && pulses-- == 0) {
			execute();
			pulses = delay;
		}
	}

	/**
	 * Sets the delay.
	 * 
	 * @param delay The delay.
	 * @throws IllegalArgumentException If the delay is less than or equal to zero.
	 */
	public final void setDelay(int delay) {
		if (delay < 0) {
			throw new IllegalArgumentException("Delay cannot be less than 0.");
		}
		this.delay = delay;
	}

	/**
	 * Stops the task.
	 */
	public void stop() {
		running = false;
	}

}