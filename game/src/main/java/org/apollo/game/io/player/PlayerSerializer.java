package org.apollo.game.io.player;

import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.util.security.PlayerCredentials;

/**
 * An interface which may be implemented by others which are capable of serializing and deserializing players. For
 * example, implementations might include text-based, binary and SQL serializers.
 *
 * @author Graham
 * @author Major
 */
public abstract class PlayerSerializer {

	/**
	 * The spawn point for Players, on Tutorial Island.
	 */
	public static final Position TUTORIAL_ISLAND_SPAWN = new Position(3093, 3104);

	/**
	 * The World this PlayerSerializer is for.
	 */
	protected final World world;

	/**
	 * Creates the PlayerSerializer.
	 *
	 * @param world The {@link World} this PlayerSerializer is for.
	 */
	public PlayerSerializer(World world) {
		this.world = world;
	}

	/**
	 * Loads a {@link Player}.
	 *
	 * @param credentials The {@link PlayerCredentials}.
	 * @return The {@link PlayerLoaderResponse}.
	 * @throws Exception If an error occurs.
	 */
	public abstract PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception;

	/**
	 * Saves a {@link Player}.
	 *
	 * @param player The Player to save.
	 * @throws Exception If an error occurs.
	 */
	public abstract void savePlayer(Player player) throws Exception;

}