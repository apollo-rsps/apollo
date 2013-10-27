package org.apollo.game.command;

import org.apollo.game.model.Player;

/**
 * An interface which should be implemented by classes to listen to
 * {@link Command}s.
 * @author Graham
 */
public interface CommandListener {

	/**
	 * Executes the action for this command.
	 * @param player The player.
	 * @param command The command.
	 */
	public void execute(Player player, Command command);

}
