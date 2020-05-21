package org.apollo;

import java.util.logging.Logger;

import org.apollo.game.model.World;
import org.apollo.game.service.GameService;
import org.apollo.game.service.LoginService;
import org.apollo.game.service.UpdateService;

/**
 * A class which manages {@link Service}s.
 *
 * @author Graham
 * @author Major
 */
public final class ServiceManager {

	/**
	 * The Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());

	/**
	 * The GameService.
	 */
	private final GameService game;

	/**
	 * The LoginService.
	 */
	private final LoginService login;

	/**
	 * The UpdateService.
	 */
	private final UpdateService update = new UpdateService();

	/**
	 * Creates and initializes the {@link ServiceManager}.
	 *
	 * @param world The {@link World} to create the {@link Service}s for.
	 * @throws Exception If there is an error creating the Services.
	 */
	public ServiceManager(World world) throws Exception {
		game = new GameService(world);
		login = new LoginService(world);
	}

	/**
	 * Gets the {@link GameService}.
	 *
	 * @return The GameService.
	 */
	public GameService getGame() {
		return game;
	}

	/**
	 * Gets the {@link LoginService}.
	 *
	 * @return The LoginService.
	 */
	public LoginService getLogin() {
		return login;
	}

	/**
	 * Gets the {@link UpdateService}.
	 *
	 * @return The UpdateService.
	 */
	public UpdateService getUpdate() {
		return update;
	}

	/**
	 * Sets the context of all services.
	 *
	 * @param context The server context.
	 */
	public void setContext(ServerContext context) {
		game.setContext(context);
		login.setContext(context);
		update.setContext(context);
	}

	/**
	 * Starts all the services.
	 */
	public void startAll() {
		logger.info("Starting services...");
		game.start();
		login.start();
		update.start();
	}

}