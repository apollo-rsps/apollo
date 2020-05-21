package org.apollo.game.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apollo.Service;
import org.apollo.game.io.player.PlayerLoaderResponse;
import org.apollo.game.io.player.PlayerSerializer;
import org.apollo.game.login.PlayerLoaderWorker;
import org.apollo.game.login.PlayerSaverWorker;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.session.GameSession;
import org.apollo.game.session.LoginSession;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;
import org.apollo.net.release.Release;
import org.apollo.util.ThreadUtil;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;
import org.xml.sax.SAXException;

/**
 * The {@link LoginService} manages {@link LoginRequest}s.
 *
 * @author Graham
 * @author Major
 */
public final class LoginService extends Service {

	/**
	 * The World this Service is for.
	 */
	protected final World world;

	/**
	 * The {@link ExecutorService} to which workers are submitted.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool(ThreadUtil.create("LoginService"));

	/**
	 * The current {@link PlayerSerializer}.
	 */
	private PlayerSerializer serializer;

	/**
	 * Creates the login service.
	 *
	 * @param world The {@link World} to log Players in to.
	 * @throws Exception If an error occurs.
	 */
	public LoginService(World world) throws Exception {
		this.world = world;
		init();
	}

	@Override
	public void start() {

	}

	/**
	 * Submits a login request.
	 *
	 * @param session The session submitting this request.
	 * @param request The login request.
	 * @throws IOException If some I/O exception occurs.
	 */
	public void submitLoadRequest(LoginSession session, LoginRequest request) throws IOException {
		int response = LoginConstants.STATUS_OK;

		if (requiresUpdate(request)) {
			response = LoginConstants.STATUS_GAME_UPDATED;
		}

		if (response == LoginConstants.STATUS_OK) {
			executor.submit(new PlayerLoaderWorker(serializer, session, request));
		} else {
			session.handlePlayerLoaderResponse(request, new PlayerLoaderResponse(response));
		}
	}

	/**
	 * Submits a save request.
	 *
	 * @param session The session submitting this request.
	 * @param player The player to save.
	 */
	public void submitSaveRequest(GameSession session, Player player) {
		executor.submit(new PlayerSaverWorker(serializer, session, player));
	}

	/**
	 * Initialises the login service.
	 *
	 * @throws SAXException If there is an error parsing the XML file.
	 * @throws IOException If there is an error accessing the file.
	 * @throws ReflectiveOperationException If the {@link PlayerSerializer} implementation could not be created.
	 */
	private void init() throws SAXException, IOException, ReflectiveOperationException {
		XmlParser parser = new XmlParser();
		XmlNode rootNode;

		try (InputStream is = new FileInputStream("data/login.xml")) {
			rootNode = parser.parse(is);
		}

		if (!rootNode.getName().equals("login")) {
			throw new IOException("Unexpected root node name, expected 'login'.");
		}

		XmlNode serializer = rootNode.getChild("serializer");
		if (serializer == null || !serializer.hasValue()) {
			throw new IOException("No serializer child node or value.");
		}

		Class<?> clazz = Class.forName(serializer.getValue());
		this.serializer = (PlayerSerializer) clazz.getConstructor(World.class).newInstance(world);
	}

	/**
	 * Checks if an update is required whenever a {@link Player} submits a login request.
	 *
	 * @param request The login request.
	 * @return {@code true} if an update is required, otherwise return {@code false}.
	 * @throws IOException If some I/O exception occurs.
	 */
	private boolean requiresUpdate(LoginRequest request) throws IOException {
		Release release = context.getRelease();
		if (release.getReleaseNumber() != request.getReleaseNumber()) {
			return true;
		}

		int[] clientCrcs = request.getArchiveCrcs();
		int[] serverCrcs = context.getFileSystem().getCrcs();

		return !Arrays.equals(clientCrcs, serverCrcs);
	}

}