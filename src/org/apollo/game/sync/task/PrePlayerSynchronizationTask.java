package org.apollo.game.sync.task;

import org.apollo.game.message.impl.RegionChangeMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Player;

/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the specified {@link Player}.
 * 
 * @author Graham
 */
public final class PrePlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PrePlayerSynchronizationTask} for the specified player.
	 * 
	 * @param player The player.
	 */
	public PrePlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	/**
	 * Checks if a region update is required.
	 * 
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	private boolean isRegionUpdateRequired() {
		Position current = player.getPosition();
		Position last = player.getLastKnownRegion();

		int deltaX = current.getLocalX(last);
		int deltaY = current.getLocalY(last);

		return deltaX < 16 || deltaX >= 88 || deltaY < 16 || deltaY >= 88;
	}

	@Override
	public void run() {
		player.getWalkingQueue().pulse();

		if (player.isTeleporting()) {
			player.resetViewingDistance();
		}

		if (!player.hasLastKnownRegion() || isRegionUpdateRequired()) {
			player.setRegionChanged(true);

			Position position = player.getPosition();
			player.setLastKnownRegion(position);

			player.send(new RegionChangeMessage(position));
		}
	}

}