package org.apollo.game.io.player;

import org.apollo.cache.def.ItemDefinition;
import org.apollo.game.database.ConnectionConfig;
import org.apollo.game.database.ConnectionPool;
import org.apollo.game.database.ConnectionSupplier;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;
import org.junit.jupiter.api.Test;

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

		PlayerCredentials credentials = new PlayerCredentials("Sino", "hello123", 0, 0, "");
		Player player = new Player(world, credentials, new Position(3093, 3493, 0));

		serializer.savePlayer(player);

		PlayerLoaderResponse response = serializer.loadPlayer(credentials);
		assertEquals(LoginConstants.STATUS_OK, response.getStatus());

		Position pos = response.getPlayer().get().getPosition();
		assertEquals(player.getPosition().getX(), pos.getX());
		assertEquals(player.getPosition().getY(), pos.getY());
		assertEquals(player.getPosition().getHeight(), pos.getHeight());
	}
}
