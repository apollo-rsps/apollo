package org.apollo.game.account;

import java.util.Objects;

/**
 * A password secret that that grants access to an account and should
 * therefore only be known by the owner of the account.
 * @author Sino
 */
public final class Password {
	public static Password of(String value) {
		return new Password(value);
	}

	private final String value;

	private Password(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Password email = (Password) o;
		return Objects.equals(value, email.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
