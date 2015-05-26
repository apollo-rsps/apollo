package org.apollo.game.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apollo.Service;
import org.apollo.game.GameConstants;
import org.apollo.game.GamePulseHandler;
import org.apollo.game.io.MessageHandlerChainSetParser;
import org.apollo.game.message.handler.MessageHandlerChainSet;
import org.apollo.game.model.World;
import org.apollo.game.model.World.RegistrationStatus;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Player;
import org.apollo.game.session.GameSession;
import org.apollo.game.sync.ClientSynchronizer;
import org.apollo.util.ThreadUtil;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * The {@link GameService} class schedules and manages the execution of the {@link GamePulseHandler} class.
 *
 * @author Graham
 */
public final class GameService extends Service {

	/**
	 * The World this Service is for.
	 */
	protected final World world;

	/**
	 * The number of times to unregister players per cycle. This is to ensure the saving threads don't get swamped with
	 * requests and slow everything down.
	 */
	private static final int UNREGISTERS_PER_CYCLE = 50;

	/**
	 * The {@link MessageHandlerChainSet}.
	 */
	private MessageHandlerChainSet messageHandlerChainSet;

	/**
	 * A queue of players to remove.
	 */
	private final Queue<Player> oldPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The scheduled executor service.
	 */
	private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(ThreadUtil
			.create("GameService"));

	/**
	 * The {@link ClientSynchronizer}.
	 */
	private ClientSynchronizer synchronizer;

	/**
	 * Creates the GameService.
	 *
	 * @param world The {@link World} the GameService is for.
	 * @throws Exception If an error occurs during initialization.
	 */
	public GameService(World world) throws Exception {
		this.world = world;
		init();
	}

	/**
	 * Finalizes the unregistration of a player.
	 *
	 * @param player The player.
	 */
	public void finalizePlayerUnregistration(Player player) {
		synchronized (this) {
			world.unregister(player);
		}
	}

	/**
	 * Gets the MessageHandlerChainSet
	 *
	 * @return The set of MessageHandlerChain's.
	 */
	public MessageHandlerChainSet getMessageHandlerChainSet() {
		return messageHandlerChainSet;
	}

	/**
	 * Called every pulse.
	 */
	public void pulse() {
		synchronized (this) {
			finalizeUnregisters();

			MobRepository<Player> players = world.getPlayerRepository();
			for (Player player : players) {
				GameSession session = player.getSession();
				if (session != null) {
					session.handlePendingMessages(messageHandlerChainSet);
				}
			}

			world.pulse();
			synchronizer.synchronize(players, world.getNpcRepository());
		}
	}

	/**
	 * Registers a {@link Player} (may block!).
	 *
	 * @param player The Player.
	 * @param session The {@link GameSession} of the Player.
	 * @return A {@link RegistrationStatus}.
	 */
	public RegistrationStatus registerPlayer(Player player, GameSession session) {
		synchronized (this) {
			RegistrationStatus status = world.register(player);
			if (status == RegistrationStatus.OK) {
				player.setSession(session);

				Region region = world.getRegionRepository().get(player.getPosition().getRegionCoordinates());
				region.addEntity(player);
			}

			return status;
		}
	}

	/**
	 * Shuts down this game service.
	 *
	 * @param natural Whether or not the shutdown was expected.
	 */
	public void shutdown(boolean natural) {
		scheduledExecutor.shutdownNow();
		// TODO: Other events that should happen upon natural or unexpected shutdown.
	}

	@Override
	public void start() {
		scheduledExecutor.scheduleAtFixedRate(new GamePulseHandler(this), GameConstants.PULSE_DELAY,
				GameConstants.PULSE_DELAY, TimeUnit.MILLISECONDS);
	}

	/**
	 * Unregisters a player. Returns immediately. The player is unregistered at the start of the next cycle.
	 *
	 * @param player The player.
	 */
	public void unregisterPlayer(Player player) {
		oldPlayers.add(player);
	}

	/**
	 * Finalizes the unregistration of Player's queued to be unregistered.
	 */
	private void finalizeUnregisters() {
		LoginService loginService = context.getLoginService();

		for (int count = 0; count < UNREGISTERS_PER_CYCLE; count++) {
			Player player = oldPlayers.poll();
			if (player == null) {
				break;
			}

			loginService.submitSaveRequest(player.getSession(), player);
		}
	}

	/**
	 * Initializes the game service.
	 *
	 * @throws IOException If there is an error accessing the file.
	 * @throws SAXException If there is an error parsing the file.
	 * @throws ReflectiveOperationException If a MessageHandler could not be created.
	 */
	private void init() throws IOException, SAXException, ReflectiveOperationException {
		try (InputStream input = new FileInputStream("data/messages.xml")) {
			MessageHandlerChainSetParser chainSetParser = new MessageHandlerChainSetParser(input);
			messageHandlerChainSet = chainSetParser.parse(world);
		}

		try (InputStream input = new FileInputStream("data/synchronizer.xml")) {
			XmlParser parser = new XmlParser();
			XmlNode root = parser.parse(input);

			if (!root.getName().equals("synchronizer")) {
				throw new IOException("Invalid root node name.");
			}

			XmlNode active = root.getChild("active");
			if (active == null || !active.hasValue()) {
				throw new IOException("No active node/value.");
			}

			Class<?> clazz = Class.forName(active.getValue());
			synchronizer = (ClientSynchronizer) clazz.newInstance();
		}
	}

}