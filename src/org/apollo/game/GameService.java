package org.apollo.game;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apollo.Service;
import org.apollo.game.message.handler.MessageHandlerChainGroup;
import org.apollo.game.model.World;
import org.apollo.game.model.World.RegistrationStatus;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.entity.Player;
import org.apollo.game.sync.ClientSynchronizer;
import org.apollo.io.MessageHandlerChainParser;
import org.apollo.login.LoginService;
import org.apollo.net.session.GameSession;
import org.apollo.util.NamedThreadFactory;
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
	 * The number of times to unregister players per cycle. This is to ensure the saving threads don't get swamped with
	 * requests and slow everything down.
	 */
	private static final int UNREGISTERS_PER_CYCLE = 50;

	/**
	 * The {@link MessageHandlerChainGroup}.
	 */
	private MessageHandlerChainGroup chainGroup;

	/**
	 * A queue of players to remove.
	 */
	private final Queue<Player> oldPlayers = new ConcurrentLinkedQueue<>();

	/**
	 * The scheduled executor service.
	 */
	private final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory(
			"GameService"));

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
		super(world);
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
	 * Gets the message handler chains.
	 * 
	 * @return The message handler chains.
	 */
	public MessageHandlerChainGroup getMessageHandlerChains() {
		return chainGroup;
	}

	/**
	 * Initializes the game service.
	 * 
	 * @throws IOException If there is an error with the file (e.g. does not exist, cannot be read, does not contain
	 *             valid nodes).
	 * @throws SAXException If there is an error parsing the file.
	 * @throws ReflectiveOperationException If a MessageHandler could not be created.
	 */
	private void init() throws IOException, SAXException, ReflectiveOperationException {
		try (InputStream is = new FileInputStream("data/messages.xml")) {
			MessageHandlerChainParser chainGroupParser = new MessageHandlerChainParser(is);
			chainGroup = chainGroupParser.parse(world);
		}

		try (InputStream is = new FileInputStream("data/synchronizer.xml")) {
			XmlParser parser = new XmlParser();
			XmlNode rootNode = parser.parse(is);

			if (!rootNode.getName().equals("synchronizer")) {
				throw new IOException("Invalid root node name.");
			}

			XmlNode activeNode = rootNode.getChild("active");
			if (activeNode == null || !activeNode.hasValue()) {
				throw new IOException("No active node/value.");
			}

			Class<?> clazz = Class.forName(activeNode.getValue());
			synchronizer = (ClientSynchronizer) clazz.newInstance();
		}
	}

	/**
	 * Called every pulse.
	 */
	public void pulse() {
		synchronized (this) {
			LoginService loginService = getContext().getService(LoginService.class);

			int unregistered = 0;
			Player old;
			while (unregistered < UNREGISTERS_PER_CYCLE && (old = oldPlayers.poll()) != null) {
				loginService.submitSaveRequest(old.getSession(), old);
				unregistered++;
			}

			for (Player p : world.getPlayerRepository()) {
				GameSession session = p.getSession();
				if (session != null) {
					session.handlePendingMessages(chainGroup);
				}
			}

			world.pulse();
			synchronizer.synchronize(world.getPlayerRepository(), world.getNpcRepository());
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
	 * Starts the game service.
	 */
	@Override
	public void start() {
		scheduledExecutor.scheduleAtFixedRate(new GamePulseHandler(this), GameConstants.PULSE_DELAY, GameConstants.PULSE_DELAY,
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

}