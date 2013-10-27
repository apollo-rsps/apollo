package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.parser.ItemDefinitionParser;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.io.EquipmentDefinitionParser;
import org.apollo.util.CharacterRepository;
import org.apollo.util.plugin.PluginManager;

/**
 * The world class is a singleton which contains objects like the
 * {@link CharacterRepository} for players and NPCs. It should only contain
 * things relevant to the in-game world and not classes which deal with I/O and
 * such (these may be better off inside some custom {@link Service} or other
 * code, however, the circumstances are rare).
 * @author Graham
 */
public final class World {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(World.class.getName());

	/**
	 * The world.
	 */
	private static final World world = new World();

	/**
	 * Represents the different status codes for registering a player.
	 * @author Graham
	 */
	public enum RegistrationStatus {

		/**
		 * Indicates the world is full.
		 */
		WORLD_FULL,

		/**
		 * Indicates that the player is already online.
		 */
		ALREADY_ONLINE,

		/**
		 * Indicates that the player was registered successfully.
		 */
		OK;
	}

	/**
	 * Gets the world.
	 * @return The world.
	 */
	public static World getWorld() {
		return world;
	}

	/**
	 * The scheduler.
	 */
	// TODO: better place than here?
	private final Scheduler scheduler = new Scheduler();

	/**
	 * The command dispatcher.
	 */
	// TODO: better place than here?
	private final CommandDispatcher dispatcher = new CommandDispatcher();

	// TODO: better place than here!!
	private PluginManager pluginManager;

	/**
	 * The {@link CharacterRepository} of {@link Player}s.
	 */
	private final CharacterRepository<Player> playerRepository = new CharacterRepository<Player>(WorldConstants.MAXIMUM_PLAYERS);

	/**
	 * Creates the world.
	 */
	private World() {

	}

	/**
	 * Initialises the world by loading definitions from the specified file
	 * system.
	 * @param release The release number.
	 * @param fs The file system.
	 * @param mgr The plugin manager. TODO move this.
	 * @throws IOException if an I/O error occurs.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager mgr) throws IOException {
		logger.info("Loading item definitions...");
		ItemDefinitionParser itemParser = new ItemDefinitionParser(fs);
		ItemDefinition[] itemDefs = itemParser.parse();
		ItemDefinition.init(itemDefs);
		logger.info("Done (loaded " + itemDefs.length + " item definitions).");

		logger.info("Loading equipment definitions...");
		int nonNull = 0;
		InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"));
		try {
			EquipmentDefinitionParser equipParser = new EquipmentDefinitionParser(is);
			EquipmentDefinition[] equipDefs = equipParser.parse();
			for (EquipmentDefinition def : equipDefs) {
				if (def != null) {
					nonNull++;
				}
			}
			EquipmentDefinition.init(equipDefs);
		} finally {
			is.close();
		}
		logger.info("Done (loaded " + nonNull + " equipment definitions).");

		this.pluginManager = mgr; // TODO move!!
	}

	/**
	 * Gets the character repository. NOTE:
	 * {@link CharacterRepository#add(Character)} and
	 * {@link CharacterRepository#remove(Character)} should not be called
	 * directly! These mutation methods are not guaranteed to work in future
	 * releases!
	 * <p>
	 * Instead, use the {@link World#register(Player)} and
	 * {@link World#unregister(Player)} methods which do the same thing and
	 * will continue to work as normal in future releases.
	 * @return The character repository.
	 */
	public CharacterRepository<Player> getPlayerRepository() {
		return playerRepository;
	}

	/**
	 * Registers the specified player.
	 * @param player The player.
	 * @return A {@link RegistrationStatus}.
	 */
	public RegistrationStatus register(final Player player) {
		if (isPlayerOnline(player.getName())) {
			return RegistrationStatus.ALREADY_ONLINE;
		}

		boolean success = playerRepository.add(player);
		if (success) {
			logger.info("Registered player: " + player + " [online=" + playerRepository.size() + "]");
			return RegistrationStatus.OK;
		} else {
			logger.warning("Failed to register player (server full): " + player + " [online=" + playerRepository.size() + "]");
			return RegistrationStatus.WORLD_FULL;
		}
	}

	/**
	 * Checks if the specified player is online.
	 * @param name The player's name.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public boolean isPlayerOnline(String name) {
		// TODO: use a hash set or map in the future?
		for (Player player : playerRepository) {
			if (player.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Unregisters the specified player.
	 * @param player The player.
	 */
	public void unregister(Player player) {
		if (playerRepository.remove(player)) {
			logger.info("Unregistered player: " + player + " [online=" + playerRepository.size() + "]");
		} else {
			logger.warning("Could not find player to unregister: " + player + "!");
		}
	}

	/**
	 * Schedules a new task.
	 * @param task The {@link ScheduledTask}.
	 */
	public void schedule(ScheduledTask task) {
		scheduler.schedule(task);
	}

	/**
	 * Calls the {@link Scheduler#pulse()} method.
	 */
	public void pulse() {
		scheduler.pulse();
	}

	/**
	 * Gets the command dispatcher. TODO should this be here?
	 * @return The command dispatcher.
	 */
	public CommandDispatcher getCommandDispatcher() {
		return dispatcher;
	}

	/**
	 * Gets the plugin manager. TODO should this be here?
	 * @return The plugin manager.
	 */
	public PluginManager getPluginManager() {
		return pluginManager;
	}

}
