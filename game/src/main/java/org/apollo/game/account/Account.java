package org.apollo.game.account;

import org.apollo.game.model.entity.setting.PrivilegeLevel;

/**
 * An account of a user which the user's player characters are tied to.
 * @author Sino
 */
public final class Account {
	public static Account of(String email, String passwordHash, PrivilegeLevel rank) {
		return new Account(email, passwordHash, rank);
	}

	private final String email;

	private final String passwordHash;

	private final PrivilegeLevel rank;

	private Account(String email, String passwordHash, PrivilegeLevel rank) {
		this.email = email;
		this.passwordHash = passwordHash;
		this.rank = rank;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
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
