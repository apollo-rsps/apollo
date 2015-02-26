package org.apollo.game.login;

import org.apollo.game.model.entity.Player;

/**
 * An interface that should be implemented for actions that should be executed when the player logs out.
 * 
 * @author Major
 */
@FunctionalInterface
public interface LogoutListener {

	/**
	 * Executes the action for this listener.
	 * 
	 * @param player The player.
	 */
	public void execute(Player player);

}