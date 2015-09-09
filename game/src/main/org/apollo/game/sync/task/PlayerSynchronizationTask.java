package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Player;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.ChatBlock;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.seg.AddPlayerSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.RemoveMobSegment;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.game.sync.seg.TeleportSegment;

/**
 * A {@link SynchronizationTask} which synchronizes the specified {@link Player} .
 *
 * @author Graham
 */
public final class PlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum amount of local players.
	 */
	private static final int MAXIMUM_LOCAL_PLAYERS = 255;

	/**
	 * The maximum number of players to load per cycle. This prevents the update packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
	 */
	private static final int NEW_PLAYERS_PER_CYCLE = 20;

	/**
	 * The Player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PlayerSynchronizationTask} for the specified {@link Player}.
	 *
	 * @param player The Player.
	 */
	public PlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		Position lastKnownRegion = player.getLastKnownRegion();
		boolean regionChanged = player.hasRegionChanged();

		SynchronizationBlockSet blockSet = player.getBlockSet();

		if (blockSet.contains(ChatBlock.class)) {
			blockSet = blockSet.clone();
			blockSet.remove(ChatBlock.class);
		}

		Position position = player.getPosition();

		SynchronizationSegment segment = (player.isTeleporting() || player.hasRegionChanged()) ?
				new TeleportSegment(blockSet, position) : new MovementSegment(blockSet, player.getDirections());

		List<Player> localPlayers = player.getLocalPlayerList();
		int oldCount = localPlayers.size();

		List<SynchronizationSegment> segments = new ArrayList<>();
		int distance = player.getViewingDistance();

		for (Iterator<Player> iterator = localPlayers.iterator(); iterator.hasNext(); ) {
			Player other = iterator.next();

			if (removeable(position, distance, other)) {
				iterator.remove();
				segments.add(new RemoveMobSegment());
			} else {
				segments.add(new MovementSegment(other.getBlockSet(), other.getDirections()));
			}
		}

		int added = 0, count = localPlayers.size();

		for (Player other : player.getWorld().getPlayerRepository()) {
			if (count >= MAXIMUM_LOCAL_PLAYERS) {
				player.flagExcessivePlayers();
				break;
			} else if (added >= NEW_PLAYERS_PER_CYCLE) {
				break;
			}

			Position local = other.getPosition();

			if (other != player && local.isWithinDistance(position, distance) && !localPlayers.contains(other)) {
				localPlayers.add(other);
				count++;
				added++;

				blockSet = other.getBlockSet();
				if (!blockSet.contains(AppearanceBlock.class)) { // TODO check if client has cached appearance
					blockSet = blockSet.clone();
					blockSet.add(SynchronizationBlock.createAppearanceBlock(other));
				}

				segments.add(new AddPlayerSegment(blockSet, other.getIndex(), local));
			}
		}

		PlayerSynchronizationMessage message = new PlayerSynchronizationMessage(lastKnownRegion, position,
				regionChanged, segment, oldCount, segments);
		player.send(message);
	}

	/**
	 * Returns whether or not the specified {@link Player} should be removed.
	 *
	 * @param position The {@link Position} of the Player being updated.
	 * @param other The Player being tested.
	 * @return {@code true} iff the specified Player should be removed.
	 */
	private boolean removeable(Position position, int distance, Player other) {
		if (other.isTeleporting() || !other.isActive()) {
			return true;
		}

		Position otherPosition = other.getPosition();
		return otherPosition.getLongestDelta(position) > distance || !otherPosition.isWithinDistance(position, distance);
	}

}