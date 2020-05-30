package org.apollo.game.io.player;

import com.lambdaworks.crypto.SCryptUtil;
import org.apollo.game.database.ConnectionConfig;
import org.apollo.game.database.ConnectionPool;
import org.apollo.game.database.ConnectionSupplier;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.apollo.game.database.PostgresDBTestHelpers.newContainer;
import static org.apollo.game.database.PostgresDBTestHelpers.newSupplier;
import static org.junit.jupiter.api.Assertions.*;

public final class JdbcPlayerSerializerTest {
	// TODO use TestContainers - each container should somehow run the apollo.sql script

	@Test
	public void serializerShouldDeserializeAPlayerFromTheDatabase() throws Exception {
		World world = new World();

		ConnectionSupplier supplier = ConnectionPool.createHikariPool(ConnectionConfig
				.builder()
				.url("jdbc:postgresql://localhost:5432/apollo")
				.username("postgres")
				.password("postgres")
				.build());

		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, supplier);

		PlayerCredentials credentials = new PlayerCredentials("Sino", "hello123", 0, 0, "");
		PlayerLoaderResponse response = serializer.loadPlayer(credentials);

		assertEquals(LoginConstants.STATUS_OK, response.getStatus());
		assertTrue(response.getPlayer().isPresent());
	}

	@Test
	public void serializerShouldReturnInvalidCredentialsIfPasswordsMismatch() throws Exception {
		World world = new World();

		ConnectionSupplier supplier = ConnectionPool.createHikariPool(ConnectionConfig
				.builder()
				.url("jdbc:postgresql://localhost:5432/apollo")
				.username("postgres")
				.password("postgres")
				.build());

		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, supplier);

		PlayerCredentials credentials = new PlayerCredentials("Sino", "hello321", 0, 0, "");
		PlayerLoaderResponse response = serializer.loadPlayer(credentials);

		assertEquals(LoginConstants.STATUS_INVALID_CREDENTIALS, response.getStatus());
		assertFalse(response.getPlayer().isPresent());
	}

	@Test
	public void serializerShouldReturnInvalidCredentialsIfAccountDoesntExistInTheDatabase() throws Exception {
		World world = new World();

		ConnectionSupplier supplier = ConnectionPool.createHikariPool(ConnectionConfig
				.builder()
				.url("jdbc:postgresql://localhost:5432/apollo")
				.username("postgres")
				.password("postgres")
				.build());

		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, supplier);

		PlayerCredentials credentials = new PlayerCredentials("Sini", "Hello123", 0, 0, "");
		PlayerLoaderResponse response = serializer.loadPlayer(credentials);

		assertEquals(LoginConstants.STATUS_INVALID_CREDENTIALS, response.getStatus());
		assertFalse(response.getPlayer().isPresent());
	}

	@Test
	public void serializerShouldSerializeAPlayerIntoTheDatabase() throws Exception {
		World world = new World();

		ConnectionSupplier supplier = ConnectionPool.createHikariPool(ConnectionConfig
				.builder()
				.url("jdbc:postgresql://localhost:5432/apollo")
				.username("postgres")
				.password("postgres")
				.build());

		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, supplier);

		PlayerCredentials credentials = new PlayerCredentials("Sino", "Hello123", 0, 0, "");
		Player player = new Player(world, credentials, new Position(3222, 3222, 0));

		serializer.savePlayer(player);
	}
}
