package org.apollo.game.model.event.impl;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.event.PlayerEvent;

/**
 * A {@link PlayerEvent} that is fired when a {@link Player} logs in.
 *
 * @author Major
 */
public final class LoginEvent extends PlayerEvent {

	/**
	 * Creates the LoginEvent.
	 *
	 * @param player The {@link Player} logging in.
	 */
	public LoginEvent(Player player) {
		super(player);
	}

}