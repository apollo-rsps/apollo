package org.apollo.game.scheduling;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

/**
 * A class which manages {@link ScheduledTask}s.
 * 
 * @author Graham
 */
public final class Scheduler {

	/**
	 * A queue of new tasks that should be added.
	 */
	private Queue<ScheduledTask> newTasks = new ArrayDeque<>();

	/**
	 * A list of currently active tasks.
	 */
	private List<ScheduledTask> tasks = new ArrayList<>();

	/**
	 * Called every pulse: executes tasks that are still pending, adds new tasks and stops old tasks.
	 */
	public void pulse() {
		ScheduledTask task;
		while ((task = newTasks.poll()) != null) {
			tasks.add(task);
		}

		for (Iterator<ScheduledTask> it = tasks.iterator(); it.hasNext();) {
			task = it.next();
			task.pulse();
			if (!task.isRunning()) {
				it.remove();
			}
		}
	}

	/**
	 * Schedules a new task.
	 * 
	 * @param task The task to schedule.
	 */
	public boolean schedule(ScheduledTask task) {
		return newTasks.add(task);
	}

}