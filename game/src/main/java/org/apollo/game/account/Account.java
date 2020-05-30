package org.apollo.game.account;

import org.apollo.game.model.entity.setting.PrivilegeLevel;

/**
 * An account of a user which the user's player characters are tied to.
 * @author Sino
 */
public final class Account {
	public static Account of(Email email, PasswordHash passwordHash, PrivilegeLevel rank) {
		return new Account(email, passwordHash, rank);
	}

	private final Email email;

	private final PasswordHash passwordHash;

	private final PrivilegeLevel rank;

	private Account(Email email, PasswordHash passwordHash, PrivilegeLevel rank) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.rank = rank;
	}

	public Email getEmail() {
		return email;
	}

	public PasswordHash getPasswordHash() {
		return passwordHash;
	}

	public PrivilegeLevel getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "Account{email=" + email + ", passwordHash=" + passwordHash + ", rank=" + rank + "}";
	}
}
