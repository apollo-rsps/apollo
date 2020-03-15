package org.apollo.game.login;

import org.apollo.game.io.player.PlayerLoaderResponse;
import org.apollo.game.io.player.PlayerSerializer;
import org.apollo.game.session.LoginSession;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which processes a single login request.
 *
 * @author Graham
 */
public final class PlayerLoaderWorker implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(PlayerLoaderWorker.class.getName());

	/**
	 * The PlayerSerializer.
	 */
	private final PlayerSerializer loader;

	/**
	 * The request.
	 */
	private final LoginRequest request;

	/**
	 * The session that submitted the request.
	 */
	private final LoginSession session;

	/**
	 * Creates a {@link PlayerLoaderWorker} which will do the work for a single player load request.
	 *
	 * @param loader The {@link PlayerSerializer}.
	 * @param session The {@link LoginSession} which initiated the request.
	 * @param request The {@link LoginRequest} object.
	 */
	public PlayerLoaderWorker(PlayerSerializer loader, LoginSession session, LoginRequest request) {
		this.loader = loader;
		this.session = session;
		this.request = request;
	}

	@Override
	public void run() {
		try {
			PlayerLoaderResponse response = loader.loadPlayer(request.getCredentials());
			session.handlePlayerLoaderResponse(request, response);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to load player's game.", e);
			session.handlePlayerLoaderResponse(request, new PlayerLoaderResponse(LoginConstants.STATUS_COULD_NOT_COMPLETE));
		}
	}

}