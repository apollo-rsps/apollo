package org.apollo.game.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link ConnectionSupplier} that consumes and provides {@link Connection}s
 * from a pool of reusable {@link Connection}s. When a connection is taken from
 * the pool, it is up to the consumer to return it back to the pool to prevent
 * leaking connections.
 * @author Sino
 */
public final class ConnectionPool implements ConnectionSupplier {
	private static String createURL(String driver, String host, int port, String databaseName) {
		return "jdbc:" + driver + "://" + host + ":" + port + "/" + databaseName;
	}

	public static ConnectionPool hikariPool(ConnectionConfig config) {
		return create(createHikariDataSource(config));
	}

	private static HikariDataSource createHikariDataSource(ConnectionConfig config) {
		HikariConfig hc = new HikariConfig();

		String url = createURL(config.getDriver(), config.getHost(), config.getPort(), config.getDatabase());

		hc.setJdbcUrl(url);
		hc.setUsername(config.getUsername());
		hc.setPassword(config.getPassword());
		hc.setAutoCommit(true);
		hc.setConnectionTimeout(config.getTimeout().toMillis());
		hc.setMaxLifetime(config.getMaxLifetime().toMillis());
		hc.setMaximumPoolSize(config.getMaximumPoolSize());

		return new HikariDataSource(hc);
	}

	public static ConnectionPool create(DataSource dataSource) {
		return new ConnectionPool(dataSource);
	}

	private DataSource dataSource;

	private ConnectionPool(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public Connection get() throws SQLException {
		return dataSource.getConnection();
	}
}
