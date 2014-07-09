package org.apollo.game.login;

import java.util.ArrayList;
import java.util.List;

import org.apollo.game.model.entity.Player;

/**
 * A class that dispatches {@link Player}s to {@link LoginListener}s.
 * 
 * @author Major
 */
public final class LoginDispatcher {

    /**
     * A {@link List} of login listeners.
     */
    private List<LoginListener> listeners = new ArrayList<>();

    /**
     * Dispatches a player to the appropriate login listener.
     * 
     * @param player The player.
     */
    public void dispatch(Player player) {
	for (LoginListener listener : listeners) {
	    listener.execute(player);
	}
    }

    /**
     * Registers a listener with this dispatcher.
     * 
     * @param listener The listener.
     */
    public void register(LoginListener listener) {
	listeners.add(listener);
    }

}