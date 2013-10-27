package org.apollo.login;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apollo.Service;
import org.apollo.game.model.Player;
import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.io.player.PlayerSaver;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;
import org.apollo.net.release.Release;
import org.apollo.net.session.GameSession;
import org.apollo.net.session.LoginSession;
import org.apollo.util.NamedThreadFactory;
import org.apollo.util.xml.XmlNode;
import org.apollo.util.xml.XmlParser;

/**
 * The {@link LoginService} manages {@link LoginRequest}s.
 * @author Graham
 */
public final class LoginService extends Service {

	/**
	 * The {@link ExecutorService} to which workers are submitted.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("LoginService"));

	/**
	 * The current {@link PlayerLoader}.
	 */
	private PlayerLoader loader;

	/**
	 * The current {@link PlayerSaver}.
	 */
	private PlayerSaver saver;

	/**
	 * Creates the login service.
	 * @throws Exception if an error occurs.
	 */
	public LoginService() throws Exception {
		init();
	}

	/**
	 * Initialises the login service.
	 * @throws Exception if an error occurs.
	 */
	private void init() throws Exception {
		XmlParser parser = new XmlParser();
		XmlNode rootNode;

		InputStream is = new FileInputStream("data/login.xml");
		try {
			rootNode = parser.parse(is);
		} finally {
			is.close();
		}

		if (!rootNode.getName().equals("login")) {
			throw new Exception("unexpected root node name");
		}

		XmlNode loaderNode = rootNode.getChild("loader");
		if (loaderNode == null || !loaderNode.hasValue()) {
			throw new Exception("no loader child node or value");
		}

		XmlNode saverNode = rootNode.getChild("saver");
		if (saverNode == null || !saverNode.hasValue()) {
			throw new Exception("no saver child node or value");
		}

		Class<?> loaderClazz = Class.forName(loaderNode.getValue());
		Class<?> saverClazz = Class.forName(saverNode.getValue());

		loader = (PlayerLoader) loaderClazz.newInstance();
		saver = (PlayerSaver) saverClazz.newInstance();
	}

	/**
	 * Submits a login request.
	 * @param session The session submitting this request.
	 * @param request The login request.
	 */
	public void submitLoadRequest(LoginSession session, LoginRequest request) {
		Release release = session.getRelease();
		if (release.getReleaseNumber() != request.getReleaseNumber()) {
			// TODO check archive 0 CRCs
			session.handlePlayerLoaderResponse(request, new PlayerLoaderResponse(LoginConstants.STATUS_GAME_UPDATED));
		} else {
			executor.submit(new PlayerLoaderWorker(loader, session, request));
		}
	}

	/**
	 * Submits a save request.
	 * @param session The session submitting this request.
	 * @param player The player to save.
	 */
	public void submitSaveRequest(GameSession session, Player player) {
		executor.submit(new PlayerSaverWorker(saver, session, player));
	}

	/**
	 * Starts the login service.
	 */
	@Override
	public void start() {
		/* empty - here for consistency with other services */
	}

}
