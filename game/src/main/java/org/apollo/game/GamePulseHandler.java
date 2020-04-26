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
	 * The Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(GamePulseHandler.class.getName());

	/**
	 * The {@link GameService}.
	 */
	private final GameService service;

	/**
	 * Creates the GamePulseHandler.
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
		} catch (Throwable throwable) {
			logger.log(Level.SEVERE, "Exception occurred during pulse!", throwable);
		}
	}

}