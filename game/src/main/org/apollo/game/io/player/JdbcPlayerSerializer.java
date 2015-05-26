package org.apollo.game.io.player;

import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.util.security.PlayerCredentials;

/**
 * A {@link PlayerSerializer} that utilises {@code JDBC} to communicate with an SQL database containing player data.
 *
 * @author Major
 */
public final class JdbcPlayerSerializer extends PlayerSerializer {

	/**
	 * Creates the JdbcPlayerSerializer.
	 *
	 * @param world The {@link World} to place the {@link Player}s in.
	 */
	public JdbcPlayerSerializer(World world) {
		super(world);
	}

	@Override
	public void savePlayer(Player player) throws Exception {
		throw new UnsupportedOperationException("JDBC saving is not supported at this time.");
	}

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		throw new UnsupportedOperationException("JDBC loading is not supported at this time.");
	}

}