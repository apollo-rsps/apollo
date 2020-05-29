package org.apollo.game.database;

import org.testcontainers.containers.PostgreSQLContainer;

public final class PostgresDBTestHelpers {
	public static PostgreSQLContainer newContainer() {
		return new PostgreSQLContainer<>("postgres:11-alpine")
				.withDatabaseName("runescape")
				.withUsername("postgres")
				.withPassword("postgres")
				.withExposedPorts(5432);
	}

	public static ConnectionSupplier newSupplier(PostgreSQLContainer container) {
		return ConnectionPool.createHikariPool(ConnectionConfig
				.builder()
				.url("jdbc:postgresql://" + container.getHost() + ":" + container.getFirstMappedPort() + "/runescape")
				.username("postgres")
				.password("postgres")
				.build());
	}

	private PostgresDBTestHelpers() { }
}
