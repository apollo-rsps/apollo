package org.apollo.game.sync.task;

import org.apollo.game.model.entity.Player;

/**
 * A {@link SynchronizationTask} which does post-synchronization work for the specified {@link Player}.
 *
 * @author Graham
 */
public final class PostPlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PostPlayerSynchronizationTask} for the specified player.
	 *
	 * @param player The player.
	 */
	public PostPlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		player.setTeleporting(false);
		player.setRegionChanged(false);
		player.resetBlockSet();

		if (!player.isExcessivePlayersSet()) {
			player.incrementViewingDistance();
		} else {
			player.decrementViewingDistance();
			player.resetExcessivePlayers();
		}
	}

}