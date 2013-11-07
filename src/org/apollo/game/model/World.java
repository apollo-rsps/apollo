package org.apollo.game.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import org.apollo.Service;
import org.apollo.fs.IndexedFileSystem;
import org.apollo.fs.decoder.ItemDefinitionDecoder;
import org.apollo.fs.decoder.NpcDefinitionDecoder;
import org.apollo.fs.decoder.ObjectDefinitionDecoder;
import org.apollo.fs.decoder.StaticObjectDecoder;
import org.apollo.game.command.CommandDispatcher;
import org.apollo.game.model.def.EquipmentDefinition;
import org.apollo.game.model.def.ItemDefinition;
import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.model.def.ObjectDefinition;
import org.apollo.game.model.obj.StaticObject;
import org.apollo.game.scheduling.ScheduledTask;
import org.apollo.game.scheduling.Scheduler;
import org.apollo.io.EquipmentDefinitionParser;
import org.apollo.util.CharacterRepository;
import org.apollo.util.plugin.PluginManager;

/**
 * The world class is a singleton which contains objects like the {@link CharacterRepository} for players and NPCs. It
 * should only contain things relevant to the in-game world and not classes which deal with I/O and such (these may be
 * better off inside some custom {@link Service} or other code, however, the circumstances are rare).
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
	private final CommandDispatcher dispatcher = new CommandDispatcher();

	/**
	 * The {@link CharacterRepository} of {@link Npc}s.
	 */
	private final CharacterRepository<Npc> npcRepository = new CharacterRepository<Npc>(WorldConstants.MAXIMUM_NPCS);

	/**
	 * The {@link CharacterRepository} of {@link Player}s.
	 */
	private final CharacterRepository<Player> playerRepository = new CharacterRepository<Player>(
			WorldConstants.MAXIMUM_PLAYERS);

	/**
	 * The {@link PluginManager}.
	 */
	// TODO: better place than here!!
	private PluginManager pluginManager;

	/**
	 * The scheduler.
	 */
	private final Scheduler scheduler = new Scheduler();

	/**
	 * Creates the world.
	 */
	private World() {
	}

	/**
	 * Gets the command dispatcher. TODO should this be here?
	 * 
	 * @return The command dispatcher.
	 */
	public CommandDispatcher getCommandDispatcher() {
		return dispatcher;
	}

	/**
	 * Gets the npc repository.
	 * 
	 * @return The npc repository.
	 */
	public CharacterRepository<Npc> getNpcRepository() {
		return npcRepository;
	}

	/**
	 * Gets the character repository. NOTE: {@link CharacterRepository#add(Character)} and
	 * {@link CharacterRepository#remove(Character)} should not be called directly! These mutation methods are not
	 * guaranteed to work in future releases!
	 * <p>
	 * Instead, use the {@link World#register(Player)} and {@link World#unregister(Player)} methods which do the same
	 * thing and will continue to work as normal in future releases.
	 * 
	 * @return The character repository.
	 */
	public CharacterRepository<Player> getPlayerRepository() {
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
	 * Initialises the world by loading definitions from the specified file system.
	 * 
	 * @param release The release number.
	 * @param fs The file system.
	 * @param manager The plugin manager. TODO move this.
	 * @throws IOException If an I/O error occurs.
	 */
	public void init(int release, IndexedFileSystem fs, PluginManager manager) throws IOException {

		ItemDefinitionDecoder itemParser = new ItemDefinitionDecoder(fs);
		ItemDefinition[] itemDefs = itemParser.decode();
		ItemDefinition.init(itemDefs);
		logger.info("Loaded " + itemDefs.length + " item definitions.");

		InputStream is = new BufferedInputStream(new FileInputStream("data/equipment-" + release + ".dat"));
		try {
			EquipmentDefinitionParser parser = new EquipmentDefinitionParser(is);
			EquipmentDefinition[] defs = parser.parse();
			EquipmentDefinition.init(defs);
			logger.info("Loaded " + defs.length + " equipment definitions.");
		} finally {
			is.close();
		}

		NpcDefinitionDecoder parser = new NpcDefinitionDecoder(fs);
		NpcDefinition[] npcDefs = parser.decode();
		NpcDefinition.init(npcDefs);
		logger.info("Loaded " + npcDefs.length + " npc definitions.");

		ObjectDefinitionDecoder objParser = new ObjectDefinitionDecoder(fs);
		ObjectDefinition[] objDefs = objParser.decode();
		ObjectDefinition.init(objDefs);
		logger.info("Loaded " + objDefs.length + " object definitions.");

		StaticObjectDecoder objectParser = new StaticObjectDecoder(fs);
		StaticObject[] objects = objectParser.decode();
		StaticObject.init(objects);
		logger.info("Loaded " + objects.length + " static objects.");

		pluginManager = manager; // TODO move!!
	}

	/**
	 * Checks if the specified player is online.
	 * 
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
	 * Calls the {@link Scheduler#pulse()} method.
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
			logger.info("Registered npc: " + npc + " [online=" + npcRepository.size() + "]");
		} else {
			logger.warning("Failed to register npc, repository capacity reached: [online=" + npcRepository.size() + "]");
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
		if (isPlayerOnline(player.getName())) {
			return RegistrationStatus.ALREADY_ONLINE;
		}

		boolean success = playerRepository.add(player);
		if (success) {
			logger.info("Registered player: " + player + " [online=" + playerRepository.size() + "]");
			return RegistrationStatus.OK;
		}
		logger.warning("Failed to register player (server full): " + player + " [online=" + playerRepository.size()
				+ "]");
		return RegistrationStatus.WORLD_FULL;
	}

	/**
	 * Schedules a new task.
	 * 
	 * @param task The {@link ScheduledTask}.
	 */
	public void schedule(ScheduledTask task) {
		scheduler.schedule(task);
	}

	/**
	 * Unregisters the specified {@link Npc}.
	 * 
	 * @param npc The npc.
	 */
	public void unregister(Npc npc) {
		if (npcRepository.remove(npc)) {
			logger.info("Unregistered npc: " + npc + " [online=" + npcRepository.size() + "]");
		} else {
			logger.warning("Could not find npc " + npc + " to unregister!");
		}
	}

	/**
	 * Unregisters the specified player.
	 * 
	 * @param player The player.
	 */
	public void unregister(Player player) {
		if (playerRepository.remove(player)) {
			logger.info("Unregistered player: " + player + " [online=" + playerRepository.size() + "]");
		} else {
			logger.warning("Could not find player to unregister: " + player + "!");
		}
	}

}