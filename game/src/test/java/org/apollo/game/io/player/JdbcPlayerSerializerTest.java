package org.apollo.game.io.player;

import org.apollo.game.database.ConnectionSupplier;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.util.security.PlayerCredentials;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.apollo.game.database.DatabaseTestHelpers.newContainer;
import static org.apollo.game.database.DatabaseTestHelpers.newSupplier;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class JdbcPlayerSerializerTest {
	@Test
	public void serializerShouldDeserializeAPlayerFromTheDatabase() throws Exception {
		try (PostgreSQLContainer container = newContainer()) {
			container.start();

			World world = new World();

			ConnectionSupplier supplier = newSupplier(container);
			JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, supplier);

			PlayerCredentials credentials = new PlayerCredentials("Sino", "Hello123", 0, 0, "");
			PlayerLoaderResponse response = serializer.loadPlayer(credentials);

			assertTrue(response.getPlayer().isPresent());

			// TODO verify
		}
	}

	@Test
	public void serializerShouldSerializeAPlayerIntoTheDatabase() throws Exception {
		try (PostgreSQLContainer container = newContainer()) {
			container.start();

			World world = new World();

			ConnectionSupplier supplier = newSupplier(container);
			JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, supplier);

			PlayerCredentials credentials = new PlayerCredentials("Sino", "Hello123", 0, 0, "");
			Player player = new Player(world, credentials, new Position(3222, 3222, 0));

			serializer.savePlayer(player);

			// TODO verify
		}
	}
}
