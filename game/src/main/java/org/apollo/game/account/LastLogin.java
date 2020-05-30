package org.apollo.game.account;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * A timestamp of when a player has last logged into the game.
 * @author Sino
 */
public final class LastLogin {
	public static LastLogin now() {
		return of(OffsetDateTime.now());
	}

	public static LastLogin of(OffsetDateTime value) {
		return new LastLogin(value);
	}

	private final OffsetDateTime value;

	private LastLogin(OffsetDateTime value) {
		this.value = value;
	}

	public OffsetDateTime getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LastLogin lastLogin = (LastLogin) o;
		return Objects.equals(value, lastLogin.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "LastLogin{value=" + value + "}";
	}
}
