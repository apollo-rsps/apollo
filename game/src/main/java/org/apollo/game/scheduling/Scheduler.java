package org.apollo.game.scheduling;

import org.apollo.util.CollectionUtil;

import java.util.*;

/**
 * A class which manages {@link ScheduledTask}s.
 *
 * @author Graham
 */
public final class Scheduler {

	/**
	 * The Queue of tasks that are pending execution.
	 */
	private final Queue<ScheduledTask> pending = new ArrayDeque<>();

	/**
	 * The List of currently active tasks.
	 */
	private final List<ScheduledTask> active = new ArrayList<>();

	/**
	 * Pulses the {@link Queue} of {@link ScheduledTask}s, removing those that are no longer running.
	 */
	public void pulse() {
		CollectionUtil.pollAll(pending, active::add);

		for (Iterator<ScheduledTask> iterator = active.iterator(); iterator.hasNext();) {
			ScheduledTask task = iterator.next();
			task.pulse();

			if (!task.isRunning()) {
				iterator.remove();
			}
		}
	}

	/**
	 * Schedules a new task.
	 *
	 * @param task The task to schedule.
	 * @return {@code true} if the task was added successfully.
	 */
	public boolean schedule(ScheduledTask task) {
		return pending.add(task);
	}

}