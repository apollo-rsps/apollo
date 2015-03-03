package org.apollo.io.player;

import org.apollo.game.model.Position;
import org.apollo.game.model.entity.Player;
import org.apollo.security.PlayerCredentials;

/**
 * An interface which may be implemented by others which are capable of serializing and deserializing players. For
 * example, implementations might include text-based, binary and SQL serializers.
 * 
 * @author Graham
 * @author Major
 */
public interface PlayerSerializer {

	/**
	 * The spawn point for Players, on Tutorial Island.
	 */
	Position TUTORIAL_ISLAND_SPAWN = new Position(3093, 3104);

	/**
	 * Loads a {@link Player}.
	 * 
	 * @param credentials The {@link PlayerCredentials}.
	 * @return The {@link PlayerLoaderResponse}.
	 * @throws Exception If an error occurs.
	 */
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception;

	/**
	 * Saves a {@link Player}.
	 * 
	 * @param player The Player to save.
	 * @throws Exception If an error occurs.
	 */
	public void savePlayer(Player player) throws Exception;

}