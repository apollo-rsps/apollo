package org.apollo.game.database;

import org.testcontainers.containers.PostgreSQLContainer;

public final class DatabaseTestHelpers {
	public static PostgreSQLContainer newContainer() {
		return new PostgreSQLContainer<>("postgres:11-alpine")
				.withDatabaseName("runescape")
				.withUsername("postgres")
				.withPassword("postgres")
				.withExposedPorts(5432);
	}

	public static ConnectionSupplier newSupplier(PostgreSQLContainer container) {
		return ConnectionPool.hikariPool(ConnectionConfig
				.builder()
				.driver("postgresql")
				.host(container.getHost())
				.port(container.getFirstMappedPort())
				.username("postgres")
				.password("postgres")
				.database("runescape")
				.build());
	}

	private DatabaseTestHelpers() { }
}
