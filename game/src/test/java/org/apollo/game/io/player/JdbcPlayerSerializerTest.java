package org.apollo.game.io.player;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.entity.attr.*;
import org.apollo.net.codec.login.LoginConstants;
import org.apollo.util.security.PlayerCredentials;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.apollo.game.model.entity.attr.AttributeMap.define;
import static org.junit.jupiter.api.Assertions.*;

public final class JdbcPlayerSerializerTest {
	private static HikariConfig config;

	@BeforeAll
	public static void setup() {
		String jdbcURL = System.getenv("POSTGRES_JDBC_URL");

		assertNotNull(jdbcURL);
		assertTrue(jdbcURL.length() > 0);

		config = new HikariConfig();
		config.setJdbcUrl(jdbcURL);

		define("my_double", new AttributeDefinition<>(0D, AttributePersistence.PERSISTENT, AttributeType.DOUBLE));
		define("my_long", new AttributeDefinition<>(0L, AttributePersistence.PERSISTENT, AttributeType.LONG));
		define("my_bool", new AttributeDefinition<>(false, AttributePersistence.PERSISTENT, AttributeType.BOOLEAN));
		define("my_string", new AttributeDefinition<>("", AttributePersistence.PERSISTENT, AttributeType.STRING));
	}

	@Test
	public void serializerShouldDeserializeAPlayerFromTheDatabase() throws Exception {
		World world = new World();

		DataSource dataSource = new HikariDataSource(config);
		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, dataSource);

		PlayerCredentials credentials = new PlayerCredentials("Sino", "hello123", 0, 0, "");
		PlayerLoaderResponse response = serializer.loadPlayer(credentials);

		assertEquals(LoginConstants.STATUS_OK, response.getStatus());
		assertTrue(response.getPlayer().isPresent());
	}

	@Test
	public void serializerShouldReturnInvalidCredentialsIfPasswordsMismatch() throws Exception {
		World world = new World();

		DataSource dataSource = new HikariDataSource(config);
		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, dataSource);

		PlayerCredentials credentials = new PlayerCredentials("Sino", "hello321", 0, 0, "");
		PlayerLoaderResponse response = serializer.loadPlayer(credentials);

		assertEquals(LoginConstants.STATUS_INVALID_CREDENTIALS, response.getStatus());
		assertFalse(response.getPlayer().isPresent());
	}

	@Test
	public void serializerShouldReturnInvalidCredentialsIfAccountDoesntExistInTheDatabase() throws Exception {
		World world = new World();

		DataSource dataSource = new HikariDataSource(config);
		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, dataSource);

		PlayerCredentials credentials = new PlayerCredentials("Sini", "Hello123", 0, 0, "");
		PlayerLoaderResponse response = serializer.loadPlayer(credentials);

		assertEquals(LoginConstants.STATUS_INVALID_CREDENTIALS, response.getStatus());
		assertFalse(response.getPlayer().isPresent());
	}

	@Test
	public void serializerShouldSerializeAPlayerIntoTheDatabase() throws Exception {
		World world = new World();

		DataSource dataSource = new HikariDataSource(config);
		JdbcPlayerSerializer serializer = JdbcPlayerSerializer.create(world, dataSource);

		PlayerCredentials credentials = new PlayerCredentials("Sino", "hello123", 0, 0, "");
		Player player = new Player(world, credentials, new Position(3093, 3493, 0));

		player.setAttribute("my_double", new NumericalAttribute(1.0D));
		player.setAttribute("my_long", new NumericalAttribute(500L));
		player.setAttribute("my_bool", new BooleanAttribute(true));
		player.setAttribute("my_string", new StringAttribute("Hello World"));

		serializer.savePlayer(player);

		PlayerLoaderResponse response = serializer.loadPlayer(credentials);
		assertEquals(LoginConstants.STATUS_OK, response.getStatus());

		Position pos = response.getPlayer().get().getPosition();
		assertEquals(player.getPosition().getX(), pos.getX());
		assertEquals(player.getPosition().getY(), pos.getY());
		assertEquals(player.getPosition().getHeight(), pos.getHeight());
	}
}
