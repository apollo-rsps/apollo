package org.apollo.game.model.event;

import org.apollo.game.model.entity.Player;

/**
 * An {@link Event} involving a {@link Player}.
 *
 * @author Major
 */
public abstract class PlayerEvent extends Event {

	/**
	 * The Player.
	 */
	private final Player player;

	/**
	 * Creates the PlayerEvent.
	 *
	 * @param player The {@link Player}.
	 */
	public PlayerEvent(Player player) {
		this.player = player;
	}

	/**
	 * Gets the {@link Player}.
	 *
	 * @return The Player.
	 */
	public Player getPlayer() {
		return player;
	}

}