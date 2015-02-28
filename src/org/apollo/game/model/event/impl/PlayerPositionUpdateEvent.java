package org.apollo.game.model.event.impl;

import org.apollo.game.model.entity.Player;
import org.apollo.game.model.event.PlayerEvent;

/**
 * A {@link PlayerEvent} created when a Player's Position is being updated.
 *
 * @author Major
 */
public final class PlayerPositionUpdateEvent extends PlayerEvent {

	/**
	 * Creates the PlayerPositionUpdateEvent.
	 *
	 * @param player The {@link Player} whose Position is being updated.
	 */
	public PlayerPositionUpdateEvent(Player player) {
		super(player);
	}

}