package org.apollo.game.login;

import org.apollo.game.model.Player;

/**
 * A class that should be extended for actions that should be executed when the player logs in.
 * 
 * @author Major
 */
public abstract class LoginListener {

	/**
	 * Executes the action for this listener.
	 * 
	 * @param player The player.
	 */
	public abstract void execute(Player player);

}