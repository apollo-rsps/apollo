package org.apollo.game.sync.task;

import java.util.concurrent.Phaser;

/**
 * A {@link SynchronizationTask} which wraps around another {@link SynchronizationTask} and notifies the specified
 * {@link Phaser} when the task has completed by calling {@link Phaser#arriveAndDeregister()}.
 * <p>
 * The phaser must have already registered this task. This can be done using the {@link Phaser#register()} or
 * {@link Phaser#bulkRegister(int)} methods.
 *
 * @author Graham
 */
public final class PhasedSynchronizationTask extends SynchronizationTask {

	/**
	 * The phaser.
	 */
	private final Phaser phaser;

	/**
	 * The task.
	 */
	private final SynchronizationTask task;

	/**
	 * Creates the phased synchronization task.
	 *
	 * @param phaser The phaser.
	 * @param task The task.
	 */
	public PhasedSynchronizationTask(Phaser phaser, SynchronizationTask task) {
		this.phaser = phaser;
		this.task = task;
	}

	@Override
	public void run() {
		try {
			task.run();
		} catch (Exception e) {
			e.printStackTrace();
			// The executor suppresses any exceptions thrown as part of the task, so we catch and print here as
			// rethrowing them does nothing.
		} finally {
			phaser.arriveAndDeregister();
		}
	}
}