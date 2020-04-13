package org.apollo.game.sync;

import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.area.RegionCoordinates;
import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.game.service.GameService;
import org.apollo.game.sync.task.*;
import org.apollo.util.ThreadUtil;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a thread pool. A {@link Phaser} is used to ensure that
 * the synchronization is complete, allowing control to return to the {@link GameService} that started the
 * synchronization. This class will scale well with machines that have multiple cores/processors. The
 * {@link SequentialClientSynchronizer} will work better on machines with a single core/processor, however, both
 * classes
 * will work.
 *
 * @author Graham
 * @author Major
 */
public final class ParallelClientSynchronizer extends ClientSynchronizer {

	/**
	 * The ExecutorService.
	 */
	private final ExecutorService executor;

	/**
	 * The Phaser.
	 */
	private final Phaser phaser = new Phaser(1);

	/**
	 * Creates the ParallelClientSynchronizer backed by a thread pool with a number of threads equal to the number of
	 * processing cores available.
	 */
	public ParallelClientSynchronizer() {
		executor = Executors.newFixedThreadPool(ThreadUtil.AVAILABLE_PROCESSORS, ThreadUtil.create("ClientSynchronizer"));
	}

	@Override
	public void synchronize(MobRepository<Player> players, MobRepository<Npc> npcs) {
		int playerCount = players.size();
		int npcCount = npcs.size();

		Map<RegionCoordinates, Set<RegionUpdateMessage>> encodes = new ConcurrentHashMap<>();
		Map<RegionCoordinates, Set<RegionUpdateMessage>> updates = new ConcurrentHashMap<>();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			SynchronizationTask task = new PrePlayerSynchronizationTask(player, encodes, updates);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(npcCount);
		for (Npc npc : npcs) {
			SynchronizationTask task = new PreNpcSynchronizationTask(npc);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			SynchronizationTask task = new PlayerSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			SynchronizationTask task = new NpcSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players) {
			SynchronizationTask task = new PostPlayerSynchronizationTask(player);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(npcCount);
		for (Npc npc : npcs) {
			SynchronizationTask task = new PostNpcSynchronizationTask(npc);
			executor.submit(new PhasedSynchronizationTask(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();
	}

}