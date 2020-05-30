package org.apollo.game.account;

import java.util.Objects;

/**
 * An e-mail address.
 * @author Sino
 */
public final class Email {
	public static Email of(String value) {
		return new Email(value);
	}

	private final String value;

	private Email(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Email email = (Email) o;
		return Objects.equals(value, email.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "Email{value=" + value + "}";
	}
}
