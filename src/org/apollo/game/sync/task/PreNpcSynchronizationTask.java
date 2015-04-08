package org.apollo.game.sync.task;

import org.apollo.game.model.entity.Npc;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified npc.
 *
 * @author Major
 */
public final class PreNpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The npc.
	 */
	private final Npc npc;

	/**
	 * Creates the {@link PreNpcSynchronizationTask} for the specified npc.
	 *
	 * @param npc The npc.
	 */
	public PreNpcSynchronizationTask(Npc npc) {
		this.npc = npc;
	}

	@Override
	public void run() {
		npc.getWalkingQueue().pulse();
	}

}