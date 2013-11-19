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
import org.apollo.game.event.handler.chain.EventHandlerChainGroup;
import org.apollo.game.model.Player;
import org.apollo.game.model.World;
import org.apollo.game.model.World.RegistrationStatus;
import org.apollo.game.sync.ClientSynchronizer;
import org.apollo.io.EventHandlerChainParser;
import org.apollo.io.RsaKeyParser;
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
	 * The {@link EventHandlerChainGroup}.
	 */
	private EventHandlerChainGroup chainGroup;

	/**
	 * A queue of players to remove.
	 */
	private final Queue<Player> oldPlayers = new ConcurrentLinkedQueue<Player>();

	/**
	 * The scheduled executor service.
	 */
	private final ScheduledExecutorService scheduledExecutor = Executors
			.newSingleThreadScheduledExecutor(new NamedThreadFactory("GameService"));

	/**
	 * The {@link ClientSynchronizer}.
	 */
	private ClientSynchronizer synchronizer;

	/**
	 * Creates the game service.
	 * 
	 * @throws Exception If an error occurs during initialization.
	 */
	public GameService() throws Exception {
		init();
	}

	/**
	 * Finalizes the unregistration of a player.
	 * 
	 * @param player The player.
	 */
	public void finalizePlayerUnregistration(Player player) {
		synchronized (this) {
			World.getWorld().unregister(player);
		}
	}

	/**
	 * Gets the event handler chains.
	 * 
	 * @return The event handler chains.
	 */
	public EventHandlerChainGroup getEventHandlerChains() {
		return chainGroup;
	}

	/**
	 * Initializes the game service.
	 * 
	 * @throws IOException If there is an error with the file (e.g. does not exist, cannot be read, does not contain
	 *             valid nodes).
	 * @throws SAXException If there is an error parsing the file.
	 * @throws ClassNotFoundException If an event handler could not be found.
	 * @throws InstantiationException If an event handler could not be instantiated.
	 * @throws IllegalAccessException If an event handler could not be accessed.
	 */
	private void init() throws IOException, SAXException, ClassNotFoundException, InstantiationException,
			IllegalAccessException {
		InputStream is = new FileInputStream("data/events.xml");
		try {
			EventHandlerChainParser chainGroupParser = new EventHandlerChainParser(is);
			chainGroup = chainGroupParser.parse();
		} finally {
			is.close();
		}

		is = new FileInputStream("data/synchronizer.xml");
		try {
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
		} finally {
			is.close();
		}

		is = new FileInputStream("data/rsa.xml");
		try {
			RsaKeyParser parser = new RsaKeyParser(is);
			parser.parse();
		} finally {
			is.close();
		}
	}

	/**
	 * Called every pulse.
	 */
	public void pulse() {
		synchronized (this) {
			LoginService loginService = getContext().getService(LoginService.class);
			World world = World.getWorld();

			int unregistered = 0;
			Player old;
			while (unregistered < UNREGISTERS_PER_CYCLE && (old = oldPlayers.poll()) != null) {
				loginService.submitSaveRequest(old.getSession(), old);
				unregistered++;
			}

			for (Player p : world.getPlayerRepository()) {
				GameSession session = p.getSession();
				if (session != null) {
					session.handlePendingEvents(chainGroup);
				}
			}

			world.pulse();

			synchronizer.synchronize();
		}
	}

	/**
	 * Registers a player (may block!).
	 * 
	 * @param player The player.
	 * @return A {@link RegistrationStatus}.
	 */
	public RegistrationStatus registerPlayer(Player player) {
		synchronized (this) {
			return World.getWorld().register(player);
		}
	}

	/**
	 * Starts the game service.
	 */
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

}