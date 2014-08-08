package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apollo.game.message.impl.NpcSynchronizationMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.game.sync.seg.AddNpcSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.RemoveMobSegment;
import org.apollo.game.sync.seg.SynchronizationSegment;

/**
 * A {@link SynchronizationTask} which synchronizes npcs with the specified {@link Player}.
 * 
 * @author Major
 */
public final class NpcSynchronizationTask extends SynchronizationTask {

	/**
	 * The maximum number of npcs to load per cycle. This prevents the update packet from becoming too large (the client
	 * uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
	 */
	private static final int NEW_NPCS_PER_CYCLE = 20;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the {@link NpcSynchronizationTask} for the specified player.
	 * 
	 * @param player The player.
	 */
	public NpcSynchronizationTask(Player player) {
		this.player = player;
	}

	@Override
	public void run() {
		List<Npc> localNpcs = player.getLocalNpcList();
		List<SynchronizationSegment> segments = new ArrayList<>();
		int oldLocalNpcs = localNpcs.size();
		final Position playerPosition = player.getPosition();

		for (Iterator<Npc> it = localNpcs.iterator(); it.hasNext();) {
			Npc npc = it.next();
			if (!npc.isActive() || npc.isTeleporting()
					|| npc.getPosition().getLongestDelta(playerPosition) > player.getViewingDistance()) {
				it.remove();
				segments.add(new RemoveMobSegment());
			} else {
				segments.add(new MovementSegment(npc.getBlockSet(), npc.getDirections()));
			}
		}

		int added = 0;

		for (Npc npc : World.getWorld().getNpcRepository()) {
			if (localNpcs.size() >= 255) {
				player.flagExcessiveNpcs();
				break;
			} else if (added >= NEW_NPCS_PER_CYCLE) {
				break;
			}

			Position npcPosition = npc.getPosition();
			if (npcPosition.isWithinDistance(playerPosition, player.getViewingDistance()) && !localNpcs.contains(npc)
					&& npcPosition.getHeight() == playerPosition.getHeight()) {
				localNpcs.add(npc);
				added++;
				npc.turnTo(npc.getFacingPosition());
				segments.add(new AddNpcSegment(npc.getBlockSet(), npc.getIndex(), npcPosition, npc.getId()));
			}
		}

		player.send(new NpcSynchronizationMessage(playerPosition, segments, oldLocalNpcs));
	}

}