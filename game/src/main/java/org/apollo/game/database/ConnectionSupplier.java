package org.apollo.game.database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Supplies {@link Connection}s to possibly remote data sources.
 * @author Sino
 */
@FunctionalInterface
public interface ConnectionSupplier {
	/**
	 * Attempts to supply a {@link Connection} that can be used to
	 * interact with a data source such as a PostgreSQL database.
	 * @return The {@link Connection} fetched.
	 * @throws SQLException Thrown
	 */
	Connection get() throws SQLException;
}
