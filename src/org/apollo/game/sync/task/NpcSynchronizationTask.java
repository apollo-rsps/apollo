package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apollo.game.event.impl.NpcSynchronizationEvent;
import org.apollo.game.model.Npc;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.seg.AddNpcSegment;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.RemoveMobSegment;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.util.MobRepository;

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
		SynchronizationBlockSet blockSet = player.getBlockSet();
		List<Npc> localNpcs = player.getLocalNpcList();
		int oldLocalNpcs = localNpcs.size();
		List<SynchronizationSegment> segments = new ArrayList<SynchronizationSegment>();

		for (Iterator<Npc> it = localNpcs.iterator(); it.hasNext();) {
			Npc npc = it.next();
			if (!npc.isActive() || npc.isTeleporting()
					|| npc.getPosition().getLongestDelta(player.getPosition()) > player.getViewingDistance()) {
				it.remove();
				segments.add(new RemoveMobSegment());
			} else {
				segments.add(new MovementSegment(npc.getBlockSet(), npc.getDirections()));
			}
		}

		int added = 0;

		MobRepository<Npc> repository = World.getWorld().getNpcRepository();
		for (Npc npc : repository) {
			if (localNpcs.size() >= 255) {
				player.flagExcessiveNpcs();
				break;
			} else if (added >= NEW_NPCS_PER_CYCLE) {
				break;
			}

			if (npc.getPosition().isWithinDistance(player.getPosition(), player.getViewingDistance())
					&& !localNpcs.contains(npc)) {
				localNpcs.add(npc);
				added++;
				npc.turnTo(npc.getFacingPosition());
				blockSet = npc.getBlockSet();
				segments.add(new AddNpcSegment(blockSet, npc.getIndex(), npc.getPosition(), npc.getNpcDefinition()
						.getId()));
			}
		}

		NpcSynchronizationEvent event = new NpcSynchronizationEvent(player.getPosition(), segments, oldLocalNpcs);
		player.send(event);
	}

}