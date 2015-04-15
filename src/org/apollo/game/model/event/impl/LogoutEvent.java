package org.apollo.game.model.event.impl;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.event.PlayerEvent;

/**
 * A {@link PlayerEvent} that is fired when a {@link Player} logs out.
 *
 * @author Major
 */
public final class LogoutEvent extends PlayerEvent {

	/**
	 * Creates the LogoutEvent.
	 *
	 * @param player The {@link Player} logging out.
	 */
	public LogoutEvent(Player player) {
		super(player);
	}

}