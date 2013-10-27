package org.apollo.io.player;

import org.apollo.game.model.Player;
import org.apollo.net.codec.login.LoginConstants;

/**
 * A response for the
 * {@link PlayerLoader#loadPlayer(org.apollo.security.PlayerCredentials)} call.
 * @author Graham
 */
public final class PlayerLoaderResponse {

	/**
	 * The status code.
	 */
	private final int status;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates a {@link PlayerLoaderResponse} with only a status code.
	 * @param status The status code.
	 * @throws IllegalArgumentException if the status code needs a
	 * {@link Player}.
	 */
	public PlayerLoaderResponse(int status) {
		if (status == LoginConstants.STATUS_OK || status == LoginConstants.STATUS_RECONNECTION_OK) {
			throw new IllegalArgumentException("player required for this status code");
		}
		this.status = status;
		this.player = null;
	}

	/**
	 * Creates a {@link PlayerLoaderResponse} with a status code and player.
	 * @param status The status code.
	 * @param player The player.
	 * @throws IllegalArgumentException if the status code does not need
	 * {@link Player}.
	 */
	public PlayerLoaderResponse(int status, Player player) {
		if (status != LoginConstants.STATUS_OK && status != LoginConstants.STATUS_RECONNECTION_OK) {
			throw new IllegalArgumentException("player required for this status code");
		}
		this.status = status;
		this.player = player;
	}

	/**
	 * Gets the status code.
	 * @return The status code.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Gets the player.
	 * @return The player, or {@code null} if there is no player in this
	 * response.
	 */
	public Player getPlayer() {
		return player;
	}

}
