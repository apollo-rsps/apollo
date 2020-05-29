package org.apollo.game.io.player;

import org.apollo.game.database.ConnectionSupplier;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;

import java.sql.Connection;

/**
 * A {@link PlayerSerializer} that utilises {@code JDBC} to communicate with an SQL database containing player data.
 *
 * @author Major
 * @author Sino
 */
public final class JdbcPlayerSerializer extends PlayerSerializer {
	/**
	 * A factory method to construct a new {@link JdbcPlayerSerializer}.
	 *
	 * @param world       The game world to feed to {@link Player} instances.
	 * @param connections The supplier of database connections.
	 * @return The {@link JdbcPlayerSerializer}.
	 */
	public static JdbcPlayerSerializer create(World world, ConnectionSupplier connections) {
		return new JdbcPlayerSerializer(world, connections);
	}

	/**
	 * Supplies this serializer with database connections.
	 */
	private final ConnectionSupplier connections;

	/**
	 * Creates the {@link JdbcPlayerSerializer}.
	 *
	 * @param world       The {@link World} to place the {@link Player}s in.
	 * @param connections The supplier of database connections.
	 */
	private JdbcPlayerSerializer(World world, ConnectionSupplier connections) {
		super(world);

		this.connections = connections;
	}

	@Override
	public void savePlayer(Player player) throws Exception {
		try (Connection connection = connections.get()) {
			// TODO
		}
	}

	@Override
	public PlayerLoaderResponse loadPlayer(PlayerCredentials credentials) throws Exception {
		try (Connection connection = connections.get()) {
			Player player = new Player(world, credentials, new Position(3222, 3222, 0));

			// TODO

			return new PlayerLoaderResponse(LoginConstants.STATUS_OK, player);
		}
	}
}