package org.apollo.game.account;

import org.apollo.game.model.entity.setting.PrivilegeLevel;

/**
 * An account of a user which the user's player characters are tied to.
 * @author Sino
 */
public final class Account {
	public static Account of(Email email, Password password, LastLogin lastLogin, PrivilegeLevel rank) {
		return new Account(email, password, lastLogin, rank);
	}

	private final Email email;

	private final Password password;

	private final LastLogin lastLogin;

	private final PrivilegeLevel rank;

	private Account(Email email, Password password, LastLogin lastLogin, PrivilegeLevel rank) {
		this.email = email;
		this.password = password;
		this.lastLogin = lastLogin;
		this.rank = rank;
	}

	public Email getEmail() {
		return email;
	}

	public Password getPassword() {
		return password;
	}

	public LastLogin getLastLogin() {
		return lastLogin;
	}

	public PrivilegeLevel getRank() {
		return rank;
	}

	@Override
	public String toString() {
		return "Account{email=" + email + ", password=" + password + ", lastLogin=" + lastLogin + ", rank=" + rank + "}";
	}
}
