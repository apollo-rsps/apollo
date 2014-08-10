package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.decoder.GameObjectDecoder;
import org.apollo.fs.decoder.ItemDefinitionDecoder;
import org.apollo.fs.decoder.NpcDefinitionDecoder;
import org.apollo.fs.decoder.ObjectDefinitionDecoder;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.login.LoginDispatcher;
import org.apollo.game.login.LogoutDispatcher;
import org.apollo.game.model.area.Sector;
import org.apollo.game.model.area.SectorRepository;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.entity.Entity;
import org.apollo.game.model.entity.GameObject;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.Player;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.io.EquipmentDefinitionParser;
import org.apollo.util.MobRepository;
import org.apollo.util.NameUtil;
import org.apollo.util.plugin.PluginManager;

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
	 * The world.
	 */
	private static final World world = new World();

	/**
	 * Gets the world.
	 * 
	 * @return The world.
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * The command dispatcher.
	 */
	private final CommandDispatcher commandDispatcher = new CommandDispatcher();

	/**
	 * The login dispatcher.
	 */
	private final LoginDispatcher loginDispatcher = new LoginDispatcher();

	/**
	 * The logout dispatcher.
	 */
	private final LogoutDispatcher logoutDispatcher = new LogoutDispatcher();

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
	 * The {@link PluginManager}. TODO: better place than here!!
	 */
	private PluginManager pluginManager;

	/**
	 * The release number (i.e. version) of this world.
	 */
	private int releaseNumber;

	/**
	 * The scheduler.
	 */
	private final Scheduler scheduler = new Scheduler();

	/**
	 * This world's {@link SectorRepository}.
	 */
	private final SectorRepository sectorRepository = new SectorRepository(false);

	/**
	 * Creates the world.
	 */
	private World() {

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
	 * Gets the {@link LoginDispatcher}.
	 * 
	 * @return The dispatcher.
	 */
	public LoginDispatcher getLoginDispatcher() {
		return loginDispatcher;
	}

	/**
	 * Gets the {@link LogoutDispatcher}.
	 * 
	 * @return The dispatcher.
	 */
	public LogoutDispatcher getLogoutDispatcher() {
		return logoutDispatcher;
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
	 * Gets the plugin manager. TODO should this be here?
	 * 
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
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
	 * Gets this world's {@link SectorRepository}.
	 * 
	 * @return The sector repository.
	 */
	public SectorRepository getSectorRepository() {
		return sectorRepository;
	}

	/**
	 * Initialises the world by loading definitions from the specified file system.
	 * 
	 * @param release The release number.
	 * @param fs The file system.
	 * @param manager The plugin manager. TODO move this.
	 * @throws IOException If an I/O error occurs.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager manager) throws Exception {
		this.releaseNumber = release;

		ItemDefinitionDecoder itemDefDecoder = new ItemDefinitionDecoder(fs);
		ItemDefinition[] itemDefs = itemDefDecoder.decode();
		ItemDefinition.init(itemDefs);
		logger.fine("Loaded " + itemDefs.length + " item definitions.");

		try (InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"))) {
			EquipmentDefinitionParser parser = new EquipmentDefinitionParser(is);
			EquipmentDefinition[] defs = parser.parse();
			EquipmentDefinition.init(defs);
			logger.fine("Loaded " + defs.length + " equipment definitions.");
		}

		NpcDefinitionDecoder npcDecoder = new NpcDefinitionDecoder(fs);
		NpcDefinition[] npcDefs = npcDecoder.decode();
		NpcDefinition.init(npcDefs);
		logger.fine("Loaded " + npcDefs.length + " npc definitions.");

		ObjectDefinitionDecoder objectDecoder = new ObjectDefinitionDecoder(fs);
		ObjectDefinition[] objDefs = objectDecoder.decode();
		ObjectDefinition.init(objDefs);
		logger.fine("Loaded " + objDefs.length + " object definitions.");

		GameObjectDecoder staticDecoder = new GameObjectDecoder(fs);
		GameObject[] objects = staticDecoder.decode();
		placeEntities(objects);
		logger.fine("Loaded " + objects.length + " static objects.");

		manager.start();
		pluginManager = manager; // TODO move!!
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
	 * Adds entities to sectors in the {@link SectorRepository}.
	 * 
	 * @param entities The entities.
	 */
	private void placeEntities(Entity... entities) {
		for (Entity entity : entities) {
			Sector sector = sectorRepository.fromPosition(entity.getPosition());
			sector.addEntity(entity);
		}
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
	public boolean register(final Npc npc) {
		boolean success = npcRepository.add(npc);

		if (success) {
			Sector sector = sectorRepository.fromPosition(npc.getPosition());
			sector.addEntity(npc);
		} else {
			logger.warning("Failed to register npc, repository capacity reached: [count=" + npcRepository.size() + "]");
		}
		return success;
	}

	/**
	 * Registers the specified player.
	 * 
	 * @param player The player.
	 * @return A {@link RegistrationStatus}.
	 */
	public RegistrationStatus register(final Player player) {
		String username = player.getUsername();
		if (isPlayerOnline(username)) {
			return RegistrationStatus.ALREADY_ONLINE;
		}

		boolean success = playerRepository.add(player);
		if (success) {
			players.put(NameUtil.encodeBase37(username), player);
			Sector sector = sectorRepository.fromPosition(player.getPosition());
			sector.addEntity(player);

			logger.info("Registered player: " + player + " [count=" + playerRepository.size() + "]");
			return RegistrationStatus.OK;
		}

		logger.warning("Failed to register player: " + player + " [count=" + playerRepository.size() + "]");
		return RegistrationStatus.WORLD_FULL;
	}

	/**
	 * Schedules a new task.
	 * 
	 * @param task The {@link ScheduledTask}.
	 */
	public boolean schedule(ScheduledTask task) {
		return scheduler.schedule(task);
	}

	/**
	 * Unregisters the specified {@link Npc}.
	 * 
	 * @param npc The npc.
	 */
	public void unregister(final Npc npc) {
		if (npcRepository.remove(npc)) {
			Sector sector = sectorRepository.fromPosition(npc.getPosition());

			if (!sector.removeEntity(npc)) {
				logger.warning("Could not remove npc from their sector.");
			}
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

			Sector sector = sectorRepository.fromPosition(player.getPosition());
			if (!sector.removeEntity(player)) {
				logger.warning("Could not remove player from their sector.");
			}

			logoutDispatcher.dispatch(player);
		} else {
			logger.warning("Could not find player " + player + " to unregister!");
		}
	}

}