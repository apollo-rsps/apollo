package org.apollo.game.database;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.apollo.game.database.PostgresDBTestHelpers.newContainer;
import static org.apollo.game.database.PostgresDBTestHelpers.newSupplier;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class ConnectionPoolTest {
	@Test
	public void poolShouldProvideUsableDatabaseConnections() throws SQLException {
		try (PostgreSQLContainer container = newContainer()) {
			container.start();

			ConnectionSupplier supplier = newSupplier(container);
			try (Connection connection = supplier.get()) {
				try (PreparedStatement stmt = connection.prepareStatement("SELECT 1=1")) {
					assertTrue(stmt.executeQuery().next());
				}
			}
		}
	}
}
