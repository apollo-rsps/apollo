package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.cache.IndexedFileSystem;
import org.apollo.cache.decoder.ItemDefinitionDecoder;
import org.apollo.cache.decoder.NpcDefinitionDecoder;
import org.apollo.cache.decoder.ObjectDefinitionDecoder;
import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.cache.def.ItemDefinition;
import org.apollo.cache.def.NpcDefinition;
import org.apollo.cache.def.ObjectDefinition;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.fs.decoder.GameObjectDecoder;
import org.apollo.game.io.EquipmentDefinitionParser;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.EntityType;
import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.obj.GameObject;
import org.apollo.game.model.event.Event;
import org.apollo.game.model.event.EventListener;
import org.apollo.game.model.event.EventListenerChainSet;
import org.apollo.game.plugin.PluginManager;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.game.scheduling.impl.NpcMovementTask;
import org.apollo.util.NameUtil;

import com.google.common.base.Preconditions;

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
	 * The ScheduledTask that moves Npcs.
	 */
	private NpcMovementTask npcMovement;

	/**
	 * The {@link MobRepository} of {@link Npc}s.
	 */
	private final MobRepository<Npc> npcRepository = new MobRepository<>(WorldConstants.MAXIMUM_NPCS);

	/**
	 * The {@link MobRepository} of {@link Player}s.
	 */
	private final MobRepository<Player> playerRepository = new MobRepository<>(WorldConstants.MAXIMUM_PLAYERS);

	/**
	 * A {@link Map} of player usernames and the player objects.
	 */
	private final Map<Long, Player> players = new HashMap<>();

	/**
	 * The {@link PluginManager}.
	 */
	private PluginManager pluginManager;

	/**
	 * This world's {@link RegionRepository}.
	 */
	private final RegionRepository regions = RegionRepository.immutable();

	/**
	 * The release number (i.e. version) of this world.
	 */
	private int releaseNumber;

	/**
	 * The scheduler.
	 */
	private final Scheduler scheduler = new Scheduler();

	/**
	 * Creates the world.
	 */
	public World() {

	}

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
	 * Gets the {@link Player} with the specified username. Note that this will return {@code null} if the player is
	 * offline.
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
	 * Initialises the world by loading definitions from the specified file system.
	 *
	 * @param release The release number.
	 * @param fs The file system.
	 * @param manager The plugin manager. TODO move this.
	 * @throws Exception If any definitions could not be loaded or there was a failure when loading plugins.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager manager) throws Exception {
		releaseNumber = release;

		ItemDefinitionDecoder itemDecoder = new ItemDefinitionDecoder(fs);
		ItemDefinition[] items = itemDecoder.decode();
		ItemDefinition.init(items);
		logger.fine("Loaded " + items.length + " item definitions.");

		try (InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"))) {
			EquipmentDefinitionParser parser = new EquipmentDefinitionParser(is);
			EquipmentDefinition[] defs = parser.parse();
			EquipmentDefinition.init(defs);
			logger.fine("Loaded " + defs.length + " equipment definitions.");
		}

		NpcDefinitionDecoder npcDecoder = new NpcDefinitionDecoder(fs);
		NpcDefinition[] npcs = npcDecoder.decode();
		NpcDefinition.init(npcs);
		logger.fine("Loaded " + npcs.length + " npc definitions.");

		ObjectDefinitionDecoder objectDecoder = new ObjectDefinitionDecoder(fs);
		ObjectDefinition[] objectDefs = objectDecoder.decode();
		ObjectDefinition.init(objectDefs);
		logger.fine("Loaded " + objectDefs.length + " object definitions.");

		GameObjectDecoder staticDecoder = new GameObjectDecoder(fs, regions);
		GameObject[] objects = staticDecoder.decode(this);
		placeEntities(objects);
		logger.fine("Loaded " + objects.length + " static objects.");

		npcMovement = new NpcMovementTask(regions); // Must be exactly here because of ordering issues.
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
	 * Adds an {@link EventListener}, listening for an {@link Event} of the specified type.
	 *
	 * @param type The type of the Event.
	 * @param listener The EventListener.
	 */
	public <E extends Event> void listenFor(Class<E> type, EventListener<E> listener) {
		events.putListener(type, listener);
	}

	/**
	 * Pulses the world.
	 */
	public void pulse() {
		scheduler.pulse();
	}

	/**
	 * Registers the specified npc.
	 *
	 * @param npc The npc.
	 * @return {@code true} if the npc registered successfully, otherwise {@code false}.
	 */
	public boolean register(Npc npc) {
		boolean success = npcRepository.add(npc);

		if (success) {
			Region region = regions.fromPosition(npc.getPosition());
			region.addEntity(npc);

			if (npc.hasBoundaries()) {
				npcMovement.addNpc(npc);
			}
		} else {
			logger.warning("Failed to register npc, repository capacity reached: [count=" + npcRepository.size() + "]");
		}
		return success;
	}

	/**
	 * Registers the specified player.
	 *
	 * @param player The player.
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
	 * Spawns the specified {@link Entity}, which must not be a {@link Player} or an {@link Npc}, which have their own
	 * register methods.
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
		if (npcRepository.remove(npc)) {
			Region region = regions.fromPosition(npc.getPosition());

			region.removeEntity(npc);
		} else {
			logger.warning("Could not find npc " + npc + " to unregister!");
		}
	}

	/**
	 * Unregisters the specified player.
	 *
	 * @param player The player.
	 */
	public void unregister(final Player player) {
		if (playerRepository.remove(player)) {
			players.remove(NameUtil.encodeBase37(player.getUsername()));
			logger.info("Unregistered player: " + player + " [count=" + playerRepository.size() + "]");

			Region region = regions.fromPosition(player.getPosition());
			region.removeEntity(player);
		} else {
			logger.warning("Could not find player " + player + " to unregister!");
		}
	}

	/**
	 * Adds entities to regions in the {@link RegionRepository}. By default, we do not notify listeners.
	 *
	 * @param entities The entities.
	 */
	private void placeEntities(Entity... entities) {
		Arrays.stream(entities).forEach(entity -> regions.fromPosition(entity.getPosition()).addEntity(entity, false));
	}

}