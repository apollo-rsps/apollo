package org.apollo.io.player;

import org.apollo.game.model.Player;

/**
 * An interface which may be implemented by others which are capable of saving
 * players. For example, implementations might include text-based, binary and
 * SQL savers.
 * @author Graham
 */
public interface PlayerSaver {

	/**
	 * Saves a player.
	 * @param player The player to save.
	 * @throws Exception if an error occurs.
	 */
	public void savePlayer(Player player) throws Exception;

}
