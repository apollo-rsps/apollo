package org.apollo.game;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.game.service.GameService;

/**
 * A class which handles the logic for each pulse of the {@link GameService}.
 *
 * @author Graham
 */
public final class GamePulseHandler implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(GamePulseHandler.class.getName());

	/**
	 * The {@link GameService}.
	 */
	private final GameService service;

	/**
	 * Creates the game pulse handler object.
	 *
	 * @param service The {@link GameService}.
	 */
	public GamePulseHandler(GameService service) {
		this.service = service;
	}

	@Override
	public void run() {
		try {
			service.pulse();
		} catch (Throwable reason) {
			logger.log(Level.SEVERE, "Exception occured during pulse!", reason);
		}
	}

}