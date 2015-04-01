package org.apollo.game.scheduling;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import org.apollo.util.CollectionUtil;

/**
 * A class which manages {@link ScheduledTask}s.
 * 
 * @author Graham
 */
public final class Scheduler {

	/**
	 * An {@link ArrayDeque} of tasks that are pending execution.
	 */
	private final Queue<ScheduledTask> pendingTasks = new ArrayDeque<>();

	/**
	 * An {@link ArrayList} of currently active tasks.
	 */
	private final List<ScheduledTask> tasks = new ArrayList<>();

	/**
	 * Called every pulse: executes tasks that are still pending, adds new tasks and stops old tasks.
	 */
	public void pulse() {
		CollectionUtil.pollAll(pendingTasks, tasks::add);

		for (Iterator<ScheduledTask> iterator = tasks.iterator(); iterator.hasNext();) {
			ScheduledTask task = iterator.next();
			task.pulse();
			if (task.isRunning()) {
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
		return pendingTasks.add(task);
	}

}