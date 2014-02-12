package org.apollo.game.login;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.Player;

/**
 * A class that dispatches {@link Player}s to {@link LogoutListener}s.
 * 
 * @author Major
 */
public final class LogoutDispatcher {

	/**
	 * A {@link List} of logout listeners.
	 */
	private List<LogoutListener> listeners = new ArrayList<>();

	/**
	 * Dispatches a player to the appropriate logout listener.
	 * 
	 * @param player The player.
	 */
	public void dispatch(Player player) {
		for (LogoutListener listen : listeners) {
			listen.execute(player);
		}
	}

	/**
	 * Registers a listener with this dispatcher.
	 * 
	 * @param listener The listener.
	 */
	public void register(LogoutListener listener) {
		listeners.add(listener);
	}

}
