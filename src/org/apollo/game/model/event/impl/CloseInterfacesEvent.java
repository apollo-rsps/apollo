package org.apollo.game.model.event.impl;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.event.Event;
import org.apollo.game.model.event.PlayerEvent;

/**
 * An {@link Event} indicating that a player's open interfaces are about to be closed.
 *
 * @author Major
 */
public final class CloseInterfacesEvent extends PlayerEvent {

	/**
	 * Creates the CloseInterfacesEvent.
	 *
	 * @param player The {@link Player} whose interfaces are being closed.
	 */
	public CloseInterfacesEvent(Player player) {
		super(player);
	}

}