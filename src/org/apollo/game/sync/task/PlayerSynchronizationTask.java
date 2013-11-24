package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apollo.game.event.impl.PlayerSynchronizationEvent;
import org.apollo.game.model.Player;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.ChatBlock;
import org.apollo.game.sync.block.SynchronizationBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.seg.AddPlayerSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.RemoveMobSegment;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.game.sync.seg.TeleportSegment;
import org.apollo.util.MobRepository;

/**
 * A {@link SynchronizationTask} which synchronizes the specified {@link Player} .
 * 
 * @author Graham
 */
public final class PlayerSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum number of players to load per cycle. This prevents the update packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
	 */
	private static final int NEW_PLAYERS_PER_CYCLE = 20;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link PlayerSynchronizationTask} for the specified player.
	 * 
	 * @param player The player.
	 */
	public PlayerSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		Position lastKnownRegion = player.getLastKnownRegion();
		boolean regionChanged = player.hasRegionChanged();

		SynchronizationSegment segment;
		SynchronizationBlockSet blockSet = player.getBlockSet();
		if (blockSet.contains(ChatBlock.class)) {
			blockSet = blockSet.clone();
			blockSet.remove(ChatBlock.class);
		}

		if (player.isTeleporting() || player.hasRegionChanged()) {
			segment = new TeleportSegment(blockSet, player.getPosition());
		} else {
			segment = new MovementSegment(blockSet, player.getDirections());
		}

		List<Player> localPlayers = player.getLocalPlayerList();
		int oldLocalPlayers = localPlayers.size();
		List<SynchronizationSegment> segments = new ArrayList<SynchronizationSegment>();
		Iterator<Player> it = localPlayers.iterator();

		for (Player local = null; it.hasNext(); local = it.next()) {
			if (!local.isActive() || local.isTeleporting()
					|| local.getPosition().getLongestDelta(player.getPosition()) > player.getViewingDistance()) {
				it.remove();
				segments.add(new RemoveMobSegment());
			} else {
				segments.add(new MovementSegment(local.getBlockSet(), local.getDirections()));
			}
		}

		int added = 0;

		MobRepository<Player> repository = World.getWorld().getPlayerRepository();
		for (Player global : repository) {
			if (localPlayers.size() >= 255) {
				player.flagExcessivePlayers();
			} else if (added < NEW_PLAYERS_PER_CYCLE) {
				// we do not check p.isActive() here, since if they are active they
				// must be in the repository
				if (global != player
						&& global.getPosition().isWithinDistance(player.getPosition(), player.getViewingDistance())
						&& !localPlayers.contains(global)) {
					localPlayers.add(global);
					added++;

					blockSet = global.getBlockSet();
					if (!blockSet.contains(AppearanceBlock.class)) {
						// TODO check if client has cached appearance
						blockSet = blockSet.clone();
						blockSet.add(SynchronizationBlock.createAppearanceBlock(global));
					}

					segments.add(new AddPlayerSegment(blockSet, global.getIndex(), global.getPosition()));
				}
				continue;
			}
			break;
		}

		PlayerSynchronizationEvent event = new PlayerSynchronizationEvent(lastKnownRegion, player.getPosition(),
				regionChanged, segment, oldLocalPlayers, segments);
		player.send(event);
	}

}