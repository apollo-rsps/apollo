package org.apollo.game.model;

import java.util.*;
import java.util.logging.Logger;

import com.google.common.base.Preconditions;
import org.apollo.Service;
import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.decoder.ItemDefinitionDecoder;
import org.apollo.cache.decoder.NpcDefinitionDecoder;
import org.apollo.cache.decoder.ObjectDefinitionDecoder;
import org.apollo.cache.map.MapIndexDecoder;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.fs.decoder.SynchronousDecoder;
import org.apollo.game.fs.decoder.WorldMapDecoder;
import org.apollo.game.fs.decoder.WorldObjectsDecoder;
import org.apollo.game.io.EquipmentDefinitionParser;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionManager;
import org.apollo.game.model.area.collision.CollisionUpdateListener;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.event.Event;
import org.apollo.game.model.event.EventListener;
import org.apollo.game.model.event.EventListenerChainSet;
import org.apollo.game.plugin.PluginManager;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.game.scheduling.impl.NpcMovementTask;
import org.apollo.util.NameUtil;

/**
 * The world class is a singleton which contains objects like the {@link MobRepository} for players and NPCs. It should
 * only contain things relevant to the in-game world and not classes which deal with I/O and such (these may be better
 * off inside some custom {@link Service} or other code, however, the circumstances are rare).
 *
 * @author Graham
 */
public final class World {

	/**
	 * Represents the different status codes for registering a player.
	 *
	 * @author Graham
	 */
	public enum RegistrationStatus {

		/**
		 * Indicates that the player is already online.
		 */
		ALREADY_ONLINE,

		/**
		 * Indicates that the player was registered successfully.
		 */
		OK,

		/**
		 * Indicates the world is full.
		 */
		WORLD_FULL;

	}

	/**
	 * A counter for the number of ticks ran.
	 */
	private long tickCounter = 0;

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());

	/**
	 * The command dispatcher.
	 */
	private final CommandDispatcher commandDispatcher = new CommandDispatcher();

	/**
	 * The EventListenerChainSet for this World.
	 */
	private final EventListenerChainSet events = new EventListenerChainSet();

	/**
	 * The {@link MobRepository} of {@link Npc}s.
	 */
	private final MobRepository<Npc> npcRepository = new MobRepository<>(WorldConstants.MAXIMUM_NPCS);

	/**
	 * The Queue of Npcs that have yet to be removed from the repository.
	 */
	private final Queue<Npc> oldNpcs = new ArrayDeque<>();

	/**
	 * The {@link MobRepository} of {@link Player}s.
	 */
	private final MobRepository<Player> playerRepository = new MobRepository<>(WorldConstants.MAXIMUM_PLAYERS);

	/**
	 * A {@link Map} of player usernames and the player objects.
	 */
	private final Map<Long, Player> players = new HashMap<>();

	/**
	 * The Queue of Npcs that have yet to be added to the repository.
	 */
	private final Queue<Npc> queuedNpcs = new ArrayDeque<>();

	/**
	 * This world's {@link RegionRepository}.
	 */
	private final RegionRepository regions = RegionRepository.immutable();

	/**
	 * This world's {@link CollisionManager}.
	 */
	private final CollisionManager collisionManager = new CollisionManager(regions);

	/**
	 * The scheduler.
	 */
	private final Scheduler scheduler = new Scheduler();

	/**
	 * The ScheduledTask that moves Npcs.
	 */
	private NpcMovementTask npcMovement;

	/**
	 * The {@link PluginManager}.
	 */
	private PluginManager pluginManager;

	/**
	 * The release number (i.e. version) of this world.
	 */
	private int releaseNumber;

	/**
	 * Gets the collision manager.
	 *
	 * @return The collision manager
	 */
	public CollisionManager getCollisionManager() { return collisionManager; }

	/**
	 * Gets the command dispatcher.
	 *
	 * @return The command dispatcher.
	 */
	public CommandDispatcher getCommandDispatcher() {
		return commandDispatcher;
	}

	/**
	 * Gets the npc repository.
	 *
	 * @return The npc repository.
	 */
	public MobRepository<Npc> getNpcRepository() {
		return npcRepository;
	}

	/**
	 * Gets the {@link Player} with the specified username. Note that this will
	 * return {@code null} if the player is offline.
	 *
	 * @param username The username.
	 * @return The player.
	 */
	public Player getPlayer(String username) {
		return players.get(NameUtil.encodeBase37(username));
	}

	/**
	 * Gets the player repository.
	 *
	 * @return The player repository.
	 */
	public MobRepository<Player> getPlayerRepository() {
		return playerRepository;
	}

	/**
	 * Gets the plugin manager.
	 *
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

	/**
	 * Gets this world's {@link RegionRepository}.
	 *
	 * @return The RegionRepository.
	 */
	public RegionRepository getRegionRepository() {
		return regions;
	}

	/**
	 * Gets the release number of this world.
	 *
	 * @return The release number.
	 */
	public int getReleaseNumber() {
		return releaseNumber;
	}

	/**
	 * Get the current value of the {@link #tickCounter} (the number of ticks since the game started).
	 *
	 * @return The current value of the tick counter;
	 */
	public long tick() {
		return tickCounter;
	}

	/**
	 * Initialises the world by loading definitions from the specified file
	 * system.
	 *
	 * @param release The release number.
	 * @param fs The file system.
	 * @param manager The plugin manager. TODO move this.
	 * @throws Exception If there was a failure when loading plugins.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager manager) throws Exception {
		releaseNumber = release;

		SynchronousDecoder firstStageDecoder = new SynchronousDecoder(
			new NpcDefinitionDecoder(fs),
			new ItemDefinitionDecoder(fs),
			new ObjectDefinitionDecoder(fs),
			new MapIndexDecoder(fs),
			EquipmentDefinitionParser.fromFile("data/equipment-" + release + "" + ".dat")
		);

		firstStageDecoder.block();

		SynchronousDecoder secondStageDecoder = new SynchronousDecoder(
			new WorldObjectsDecoder(fs, this, regions),
			new WorldMapDecoder(fs, collisionManager)
		);

		secondStageDecoder.block();

		// Build collision matrices for the first time
		collisionManager.build(false);
		regions.addRegionListener(new CollisionUpdateListener(collisionManager));

		npcMovement = new NpcMovementTask(collisionManager); // Must be exactly here because of ordering issues.
		scheduler.schedule(npcMovement);

		manager.start();
		commandDispatcher.init(manager.getAuthors());
		pluginManager = manager;
	}

	/**
	 * Checks if the {@link Player} with the specified name is online.
	 *
	 * @param username The name.
	 * @return {@code true} if the player is online, otherwise {@code false}.
	 */
	public boolean isPlayerOnline(String username) {
		return players.get(NameUtil.encodeBase37(username)) != null;
	}

	/**
	 * Adds an {@link EventListener}, listening for an {@link Event} of the
	 * specified type.
	 *
	 * @param type The type of the Event.
	 * @param listener The EventListener.
	 */
	public <E extends Event> void listenFor(Class<E> type, EventListener<E> listener) {
		events.putListener(type, listener);
	}

	/**
	 * Pulses this World.
	 */
	public void pulse() {
		unregisterNpcs();
		registerNpcs();
		scheduler.pulse();
		tickCounter++;
	}

	/**
	 * Registers the specified {@link Npc}.
	 *
	 * @param npc The Npc.
	 */
	public void register(Npc npc) {
		queuedNpcs.add(npc);
	}

	/**
	 * Registers the specified {@link Player}.
	 *
	 * @param player The Player.
	 */
	public void register(Player player) {
		String username = player.getUsername();

		playerRepository.add(player);
		players.put(NameUtil.encodeBase37(username), player);

		logger.info("Registered player: " + player + " [count=" + playerRepository.size() + "]");
	}

	/**
	 * Schedules a new task.
	 *
	 * @param task The {@link ScheduledTask}.
	 * @return {@code true} if the task was added successfully.
	 */
	public boolean schedule(ScheduledTask task) {
		return scheduler.schedule(task);
	}

	/**
	 * Spawns the specified {@link Entity}, which must not be a {@link Player}
	 * or an {@link Npc}, which have their own register methods.
	 *
	 * @param entity The Entity.
	 */
	public void spawn(Entity entity) {
		EntityType type = entity.getEntityType();
		Preconditions.checkArgument(type != EntityType.PLAYER && type != EntityType.NPC, "Cannot spawn a Mob.");

		Region region = regions.fromPosition(entity.getPosition());
		region.addEntity(entity);
	}

	/**
	 * Submits the specified {@link Event}, passing it to the listeners..
	 *
	 * @param event The Event.
	 * @return {@code true} if the Event should proceed, {@code false} if not.
	 */
	public boolean submit(Event event) {
		return events.notify(event);
	}

	/**
	 * Unregisters the specified {@link Npc}.
	 *
	 * @param npc The npc.
	 */
	public void unregister(final Npc npc) {
		Preconditions.checkNotNull(npc, "Npc must not be null.");
		oldNpcs.add(npc);
	}

	/**
	 * Unregisters the specified player.
	 *
	 * @param player The player.
	 */
	public void unregister(final Player player) {
		Preconditions.checkNotNull(player, "Player may not be null.");
		players.remove(NameUtil.encodeBase37(player.getUsername()));

		Region region = regions.fromPosition(player.getPosition());
		region.removeEntity(player);

		playerRepository.remove(player);
		logger.info("Unregistered player: " + player + " [count=" + playerRepository.size() + "]");
	}

	/**
	 * Adds entities to regions in the {@link RegionRepository}. By default, we
	 * do not notify listeners.
	 *
	 * @param entities The entities.
	 */
	private void placeEntities(Entity... entities) {
		Arrays.stream(entities).forEach(entity -> regions.fromPosition(entity.getPosition()).addEntity(entity, false));
	}

	/**
	 * Registers all of the {@link Npc}s in the {@link #queuedNpcs queue}.
	 */
	private void registerNpcs() {
		while (!queuedNpcs.isEmpty()) {
			Npc npc = queuedNpcs.poll();
			boolean success = npcRepository.add(npc);

			if (success) {
				Region region = regions.fromPosition(npc.getPosition());
				region.addEntity(npc);

				if (npc.hasBoundaries()) {
					npcMovement.addNpc(npc);
				}
			} else {
				logger.warning("Failed to register npc (capacity reached): [count=" + npcRepository.size() + "]");
			}
		}
	}

	/**
	 * Unregisters all of the {@link Npc}s in the {@link #oldNpcs queue}.
	 */
	private void unregisterNpcs() {
		while (!oldNpcs.isEmpty()) {
			Npc npc = oldNpcs.poll();

			Region region = regions.fromPosition(npc.getPosition());
			region.removeEntity(npc);

			npcRepository.remove(npc);
		}
	}

}