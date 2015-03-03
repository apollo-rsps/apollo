package org.apollo.login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apollo.Service;
import org.apollo.game.model.entity.Player;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.io.player.PlayerSerializer;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;
import org.apollo.net.release.Release;
import org.apollo.net.session.GameSession;
import org.apollo.net.session.LoginSession;
import org.apollo.util.NamedThreadFactory;
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
	 * The {@link ExecutorService} to which workers are submitted.
	 */
	private final ExecutorService executor = Executors.newCachedThreadPool(new NamedThreadFactory("LoginService"));

	/**
	 * The current {@link PlayerSerializer}.
	 */
	private PlayerSerializer serializer;

	/**
	 * Creates the login service.
	 * 
	 * @throws Exception If an error occurs.
	 */
	public LoginService() throws Exception {
		init();
	}

	/**
	 * Initialises the login service.
	 * 
	 * @throws SAXException If there is an error parsing the XML file.
	 * @throws IOException If there is an error accessing the file.
	 * @throws ClassNotFoundException If the player loader/saver implementation could not be found.
	 * @throws IllegalAccessException If the player loader/saver implementation could not be accessed.
	 * @throws InstantiationException If the player loader/saver implementation could not be instantiated.
	 */
	private void init() throws SAXException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
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
		this.serializer = (PlayerSerializer) clazz.newInstance();
	}

	/**
	 * Starts the login service.
	 */
	@Override
	public void start() {

	}

	/**
	 * Submits a login request.
	 * 
	 * @param session The session submitting this request.
	 * @param request The login request.
	 */
	public void submitLoadRequest(LoginSession session, LoginRequest request) {
		Release release = session.getRelease();
		if (release.getReleaseNumber() != request.getReleaseNumber()) {
			// TODO check archive 0 CRCs
			session.handlePlayerLoaderResponse(request, new PlayerLoaderResponse(LoginConstants.STATUS_GAME_UPDATED));
		} else {
			executor.submit(new PlayerLoaderWorker(serializer, session, request));
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

}