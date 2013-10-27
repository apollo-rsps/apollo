package org.apollo.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.io.player.PlayerLoader;
import org.apollo.io.player.PlayerLoaderResponse;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.net.codec.login.LoginRequest;
import org.apollo.net.session.LoginSession;

/**
 * A class which processes a single login request.
 * @author Graham
 */
public final class PlayerLoaderWorker implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(PlayerLoaderWorker.class.getName());

	/**
	 * The player loader.
	 */
	private final PlayerLoader loader;

	/**
	 * The session that submitted the request.
	 */
	private final LoginSession session;

	/**
	 * The request.
	 */
	private final LoginRequest request;

	/**
	 * Creates a {@link PlayerLoaderWorker} which will do the work for a single
	 * player load request.
	 * @param loader The current player loader.
	 * @param session The {@link LoginSession} which initiated the request.
	 * @param request The {@link LoginRequest} object.
	 */
	public PlayerLoaderWorker(PlayerLoader loader, LoginSession session, LoginRequest request) {
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
