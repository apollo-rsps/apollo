package org.apollo.game.sync;

import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;

/**
 * The {@link ClientSynchronizer} manages the update sequence which keeps clients synchronized with the in-game world.
 * There are two implementations distributed with Apollo: {@link SequentialClientSynchronizer} which is optimized for a
 * single-core/single-processor machine and {@link ParallelClientSynchronizer} which is optimized for a multi-processor/
 * multi-core machines.
 * <p>
 * To switch between the two synchronizer implementations, edit the {@code synchronizers.xml} configuration file. The
 * default implementation is currently {@link ParallelClientSynchronizer} as the vast majority of machines today have
 * two or more cores.
 *
 * @author Graham
 */
public abstract class ClientSynchronizer {

	/**
	 * Synchronizes the state of the clients with the state of the server.
	 *
	 * @param players The {@link MobRepository} containing the {@link Player}s.
	 * @param npcs The {@link MobRepository} containing the {@link Npc}s.
	 */
	public abstract void synchronize(MobRepository<Player> players, MobRepository<Npc> npcs);

}