package org.apollo.game.sync.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apollo.game.message.impl.NpcSynchronizationMessage;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.EntityType;
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
	 * The maximum amount of local npcs.
	 */
	private static final int MAXIMUM_LOCAL_NPCS = 255;

	/**
	 * The maximum number of npcs to load per cycle. This prevents the update packet from becoming too large (the
	 * client uses a 5000 byte buffer) and also stops old spec PCs from crashing when they login or teleport.
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
		List<Npc> locals = player.getLocalNpcList();
		List<SynchronizationSegment> segments = new ArrayList<>();

		int originalCount = locals.size();
		final Position playerPosition = player.getPosition();

		int distance = player.getViewingDistance();
		for (Iterator<Npc> iterator = locals.iterator(); iterator.hasNext(); ) {
			Npc npc = iterator.next();
			Position position = npc.getPosition();

			if (!npc.isActive() || npc.isTeleporting() || position.getLongestDelta(playerPosition) > distance
					|| !position.isWithinDistance(playerPosition, distance)) {
				iterator.remove();

				segments.add(new RemoveMobSegment());
			} else {
				segments.add(new MovementSegment(npc.getBlockSet(), npc.getDirections()));
			}
		}

		int added = 0, count = locals.size();

		RegionRepository repository = player.getWorld().getRegionRepository();
		Region current = repository.fromPosition(playerPosition);

		Set<RegionCoordinates> regions = current.getSurrounding();
		regions.add(current.getCoordinates());

		Stream<Npc> npcs = regions.stream().map(repository::get)
				.flatMap(region -> region.getEntities(EntityType.NPC));

		Iterator<Npc> iterator = npcs.iterator();

		while (iterator.hasNext()) {
			if (count >= MAXIMUM_LOCAL_NPCS) {
				player.flagExcessiveNpcs();
				break;
			} else if (added >= NEW_NPCS_PER_CYCLE) {
				break;
			}

			Npc npc = iterator.next();
			Position position = npc.getPosition();
			if (position.isWithinDistance(playerPosition, distance) && !locals.contains(npc)) {
				locals.add(npc);
				count++;
				added++;

				npc.turnTo(npc.getFacingPosition());
				segments.add(new AddNpcSegment(npc.getBlockSet(), npc.getIndex(), position, npc.getId()));
			}
		}

		player.send(new NpcSynchronizationMessage(playerPosition, segments, originalCount));
	}

}