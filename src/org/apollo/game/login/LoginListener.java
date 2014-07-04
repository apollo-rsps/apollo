package org.apollo.game.login;

import org.apollo.game.model.entity.Player;

/**
 * A class that should be extended for actions that should be executed when the player logs in.
 * 
 * @author Major
 */
@FunctionalInterface
public interface LoginListener {

	/**
	 * Executes the action for this listener.
	 * 
	 * @param player The player.
	 */
	public abstract void execute(Player player);

}