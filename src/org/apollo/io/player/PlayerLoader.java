package org.apollo.io.player;

import org.apollo.security.PlayerCredentials;

/**
 * An interface which may be extended by others which are capable of loading
 * players. For example, implementations might include text-based, binary and
 * SQL loaders.
 * @author Graham
 */
public interface PlayerLoader {

	/**
	 * Loads a player.
	 * @param credentials The player's credentials.
	 * @return The {@link PlayerLoaderResponse}.
	 * @throws Exception if an error occurs.
	 */
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception;

}
