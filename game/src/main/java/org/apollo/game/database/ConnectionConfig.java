package org.apollo.game.database;

import java.time.Duration;

import static java.util.Objects.requireNonNull;

/**
 * Contains configurations specifically for (remote) database connections.
 * @author Sino
 */
public final class ConnectionConfig {
	private static final Duration DEFAULT_MAX_LIFE_TIME = Duration.ofSeconds(30);

	private static final Duration DEFAULT_CONNECTION_TIMEOUT = Duration.ofSeconds(8);

	public static class Builder {
		private String driver;

		private String host;

		private int port;

		private String database;

		private String username, password;

		private Duration timeout = DEFAULT_CONNECTION_TIMEOUT;

		private Duration maxLifetime = DEFAULT_MAX_LIFE_TIME;

		private int maximumPoolSize = 1;

		public Builder driver(String driver) {
			this.driver = driver;

			return this;
		}

		public Builder host(String host) {
			this.host = host;

			return this;
		}

		public Builder port(int port) {
			this.port = port;

			return this;
		}

		public Builder database(String database) {
			this.database = database;

			return this;
		}

		public Builder username(String username) {
			this.username = username;

			return this;
		}

		public Builder password(String password) {
			this.password = password;

			return this;
		}

		public Builder timeout(Duration timeout) {
			this.timeout = timeout;

			return this;
		}

		public Builder maxLifetime(Duration maxLifetime) {
			this.maxLifetime = maxLifetime;

			return this;
		}

		public Builder maximumPoolSize(int maximumPoolSize) {
			this.maximumPoolSize = maximumPoolSize;

			return this;
		}

		public ConnectionConfig build() {
			return new ConnectionConfig(
					requireNonNull(driver),
					requireNonNull(host),
					port,
					requireNonNull(database),
					requireNonNull(username),
					requireNonNull(password),
					requireNonNull(timeout),
					requireNonNull(maxLifetime),
					maximumPoolSize);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	private final String driver;

	private final String host;

	private final int port;

	private final String database;

	private final String username, password;

	private final Duration timeout;

	private final Duration maxLifetime;

	private final int maximumPoolSize;

	private ConnectionConfig(String driver, String host, int port, String database, String username, String password, Duration timeout, Duration maxLifetime, int maximumPoolSize) {
		this.driver = driver;
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
		this.timeout = timeout;
		this.maxLifetime = maxLifetime;
		this.maximumPoolSize = maximumPoolSize;
	}

	public String getDriver() {
		return driver;
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getDatabase() {
		return database;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public Duration getTimeout() {
		return timeout;
	}

	public Duration getMaxLifetime() {
		return maxLifetime;
	}

	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}

	@Override
	public String toString() {
		return "ConnectionConfig{driver = " + driver +
				", host=" + host +
				", port=" + port +
				", database=" + database +
				", username=" + username +
				", password=" + password +
				", timeout=" + timeout +
				", maxLifetime=" + maxLifetime +
				", maximumPoolSize=" + maximumPoolSize + "}";
	}
}
