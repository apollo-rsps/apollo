package org.apollo.game.io.player;

import com.google.common.base.Preconditions;
import org.apollo.game.model.entity.Player;
import org.apollo.net.codec.login.LoginConstants;

import java.util.Optional;

/**
 * A response for the {@link PlayerSerializer#loadPlayer} call.
 *
 * @author Graham
 * @author Major
 */
public final class PlayerLoaderResponse {

	/**
	 * The player.
	 */
	private final Optional<Player> player;

	/**
	 * The status code.
	 */
	private final int status;

	/**
	 * Creates a {@link PlayerLoaderResponse} with only a status code.
	 *
	 * @param status The status code.
	 * @throws IllegalArgumentException If the status code is {@link LoginConstants#STATUS_OK} or
	 *             {@link LoginConstants#STATUS_RECONNECTION_OK}.
	 */
	public PlayerLoaderResponse(int status) {
		Preconditions.checkArgument(status != LoginConstants.STATUS_OK && status != LoginConstants.STATUS_RECONNECTION_OK, "Player required for this status code.");
		this.status = status;
		player = Optional.empty();
	}

	/**
	 * Creates a {@link PlayerLoaderResponse} with a status code and {@link Player}.
	 *
	 * @param status The status code.
	 * @param player The player.
	 * @throws IllegalArgumentException If the status code does not need a player.
	 * @throws NullPointerException If the specified player is null.
	 */
	public PlayerLoaderResponse(int status, Player player) {
		Preconditions.checkArgument(status == LoginConstants.STATUS_OK || status == LoginConstants.STATUS_RECONNECTION_OK, "Player not required for this status code.");
		this.status = status;
		this.player = Optional.of(player);
	}

	/**
	 * Gets the player.
	 *
	 * @return The player, wrapped in an {@link Optional}.
	 */
	public Optional<Player> getPlayer() {
		return player;
	}

	/**
	 * Gets the status code.
	 *
	 * @return The status code.
	 */
	public int getStatus() {
		return status;
	}

}