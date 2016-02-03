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
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.MobRepository;
import org.apollo.game.model.entity.Player;
import org.apollo.game.session.GameSession;
import org.apollo.game.session.LoginSession;
import org.apollo.game.sync.ClientSynchronizer;
import org.apollo.net.codec.login.LoginConstants;
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
	 * A utility class wrapping a {@link Player} and their corresponding {@link LoginSession}.
	 */
	private static final class LoginPlayerRequest {

		/**
		 * The Player.
		 */
		private final Player player;

		/**
		 * The LoginSession.
		 */
		private final LoginSession session;

		/**
		 * Creates the LoginPlayerRequest.
		 *
		 * @param player The {@link Player} logging in.
		 * @param session The {@link LoginSession} of the Player.
		 */
		public LoginPlayerRequest(Player player, LoginSession session) {
			this.player = player;
			this.session = session;
		}

	}

	/**
	 * The amount of players to deregister per cycle. This is to ensure the saving threads don't get swamped with
	 * requests and slow everything down.
	 */
	private static final int DEREGISTRATIONS_PER_CYCLE = 50;

	/**
	 * The amount of players to register per cycle.
	 */
	private static final int REGISTRATIONS_PER_CYCLE = 25;

	/**
	 * The World this Service is for.
	 */
	protected final World world;

	/**
	 * The scheduled executor service.
	 */
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(ThreadUtil.create("GameService"));

	/**
	 * The Queue of LoginPlayers to add.
	 */
	private final Queue<LoginPlayerRequest> newPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The Queue of Players to remove.
	 */
	private final Queue<Player> oldPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The {@link MessageHandlerChainSet}.
	 */
	private MessageHandlerChainSet handlers;

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
	 * Finalizes the registration of a player.
	 *
	 * @param player The player.
	 */
	public synchronized void finalizePlayerRegistration(Player player) {
		world.register(player);
		Region region = world.getRegionRepository().fromPosition(player.getPosition());
		region.addEntity(player);

		if (!player.getSession().isReconnecting()) {
			player.sendInitialMessages();
		}
	}

	/**
	 * Finalizes the unregistration of a player.
	 *
	 * @param player The player.
	 */
	public synchronized void finalizePlayerUnregistration(Player player) {
		world.unregister(player);
	}

	/**
	 * Gets the MessageHandlerChainSet
	 *
	 * @return The MessageHandlerChainSet.
	 */
	public MessageHandlerChainSet getMessageHandlerChainSet() {
		return handlers;
	}

	/**
	 * Called every pulse.
	 */
	public synchronized void pulse() {
		finalizeRegistrations();
		finalizeUnregistrations();

		MobRepository<Player> players = world.getPlayerRepository();
		for (Player player : players) {
			GameSession session = player.getSession();

			if (session != null) {
				session.handlePendingMessages(handlers);
			}
		}

		world.pulse();
		synchronizer.synchronize(players, world.getNpcRepository());
	}

	/**
	 * Registers a {@link Player} at the end of the next cycle.
	 *
	 * @param player The Player to register.
	 * @param session the {@link LoginSession} of the Player.
	 */
	public void registerPlayer(Player player, LoginSession session) {
		newPlayers.add(new LoginPlayerRequest(player, session));
	}

	/**
	 * Shuts down this game service.
	 *
	 * @param natural Whether or not the shutdown was expected.
	 */
	public void shutdown(boolean natural) {
		executor.shutdownNow();
		// TODO: Other events that should happen upon natural or unexpected shutdown.
	}

	@Override
	public void start() {
		executor.scheduleAtFixedRate(new GamePulseHandler(this), GameConstants.PULSE_DELAY, GameConstants.PULSE_DELAY,
				TimeUnit.MILLISECONDS);
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
	 * Finalizes the registration of Player's queued to be registered.
	 */
	private void finalizeRegistrations() {
		for (int count = 0; count < REGISTRATIONS_PER_CYCLE; count++) {
			LoginPlayerRequest request = newPlayers.poll();
			if (request == null) {
				break;
			}

			Player player = request.player;
			if (world.isPlayerOnline(player.getUsername())) {
				request.session.sendLoginFailure(LoginConstants.STATUS_ACCOUNT_ONLINE);
			} else if (world.getPlayerRepository().full()) {
				request.session.sendLoginFailure(LoginConstants.STATUS_SERVER_FULL);
			} else {
				request.session.sendLoginSuccess(player);
				finalizePlayerRegistration(player);
			}
		}
	}

	/**
	 * Finalizes the unregistration of Player's queued to be unregistered.
	 */
	private void finalizeUnregistrations() {
		LoginService loginService = context.getLoginService();

		for (int count = 0; count < DEREGISTRATIONS_PER_CYCLE; count++) {
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
			handlers = chainSetParser.parse(world);
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
