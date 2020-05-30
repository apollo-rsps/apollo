package org.apollo.game.account;

import java.util.Objects;

/**
 * The hash value of a password secret that that grants access to an
 * account and should therefore only be known by the owner of the account.
 * @author Sino
 */
public final class PasswordHash {
	public static PasswordHash of(String value) {
		return new PasswordHash(value);
	}

	private final String value;

	private PasswordHash(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		PasswordHash email = (PasswordHash) o;
		return Objects.equals(value, email.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
