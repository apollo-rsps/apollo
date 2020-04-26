package org.apollo.game.login;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apollo.game.io.player.PlayerSerializer;
import org.apollo.game.model.entity.Player;
import org.apollo.game.session.GameSession;

/**
 * A class which processes a single save request.
 *
 * @author Graham
 */
public final class PlayerSaverWorker implements Runnable {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(PlayerSaverWorker.class.getName());

	/**
	 * The player to save.
	 */
	private final Player player;

	/**
	 * The player saver.
	 */
	private final PlayerSerializer saver;

	/**
	 * The game session.
	 */
	private final GameSession session;

	/**
	 * Creates the player saver worker.
	 *
	 * @param saver The player saver.
	 * @param session The game session.
	 * @param player The player to save.
	 */
	public PlayerSaverWorker(PlayerSerializer saver, GameSession session, Player player) {
		this.saver = saver;
		this.session = session;
		this.player = player;
	}

	@Override
	public void run() {
		try {
			saver.savePlayer(player);
			session.handlePlayerSaverResponse(true);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Unable to save player's game.", e);
			session.handlePlayerSaverResponse(false);
		}
	}

}