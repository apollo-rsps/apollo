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
		private String url;

		private String username, password;

		private Duration timeout = DEFAULT_CONNECTION_TIMEOUT;

		private Duration maxLifetime = DEFAULT_MAX_LIFE_TIME;

		private int maximumPoolSize = 1;

		public Builder url(String url) {
			this.url = url;

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
					requireNonNull(url),
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

	private final String url;

	private final String username, password;

	private final Duration timeout;

	private final Duration maxLifetime;

	private final int maximumPoolSize;

	private ConnectionConfig(String url, String username, String password, Duration timeout, Duration maxLifetime, int maximumPoolSize) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.timeout = timeout;
		this.maxLifetime = maxLifetime;
		this.maximumPoolSize = maximumPoolSize;
	}

	public String getUrl() {
		return url;
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
		return "ConnectionConfig{url = " + url +
				", username=" + username +
				", password=" + password +
				", timeout=" + timeout +
				", maxLifetime=" + maxLifetime +
				", maximumPoolSize=" + maximumPoolSize + "}";
	}
}
