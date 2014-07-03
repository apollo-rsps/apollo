package org.apollo.game.sync;

import org.apollo.game.GameService;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.game.sync.task.NpcSynchronizationTask;
import org.apollo.game.sync.task.PlayerSynchronizationTask;
import org.apollo.game.sync.task.PostNpcSynchronizationTask;
import org.apollo.game.sync.task.PostPlayerSynchronizationTask;
import org.apollo.game.sync.task.PreNpcSynchronizationTask;
import org.apollo.game.sync.task.PrePlayerSynchronizationTask;
import org.apollo.game.sync.task.SynchronizationTask;
import org.apollo.util.MobRepository;

/**
 * An implementation of {@link ClientSynchronizer} which runs in a single thread (the {@link GameService} thread from
 * which this is called). Each client is processed sequentially. Therefore this class will work well on machines with a
 * single core/processor. The {@link ParallelClientSynchronizer} will work better on machines with multiple
 * cores/processors, however, both classes will work.
 * 
 * @author Graham
 * @author Major
 */
public final class SequentialClientSynchronizer extends ClientSynchronizer {

	@Override
	public void synchronize() {
		MobRepository<Player> players = World.getWorld().getPlayerRepository();
		MobRepository<Npc> npcs = World.getWorld().getNpcRepository();

		for (Player player : players) {
			SynchronizationTask task = new PrePlayerSynchronizationTask(player);
			task.run();
		}

		for (Npc npc : npcs) {
			SynchronizationTask task = new PreNpcSynchronizationTask(npc);
			task.run();
		}

		for (Player player : players) {
			SynchronizationTask task = new PlayerSynchronizationTask(player);
			task.run();
			task = new NpcSynchronizationTask(player);
			task.run();
		}

		for (Player player : players) {
			SynchronizationTask task = new PostPlayerSynchronizationTask(player);
			task.run();
		}

		for (Npc npc : npcs) {
			SynchronizationTask task = new PostNpcSynchronizationTask(npc);
			task.run();
		}
	}

}