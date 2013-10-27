package org.apollo.game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which handles the logic for each pulse of the {@link GameService}.
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
	 * @param service The {@link GameService}.
	 */
	GamePulseHandler(GameService service) {
		this.service = service;
	}

	@Override
	public void run() {
		try {
			service.pulse();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Exception during pulse.", t);
		}
	}

}
