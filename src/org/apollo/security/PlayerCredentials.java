package org.apollo.security;

import org.apollo.util.NameUtil;

import com.lambdaworks.crypto.SCryptUtil;

/**
 * Holds the credentials for a player.
 * 
 * @author Graham
 */
public final class PlayerCredentials {

	/**
	 * The player's username encoded as a long.
	 */
	private final long encodedUsername;

	/**
	 * The player's password.
	 */
	private String password;

	/**
	 * The computer's unique identifier.
	 */
	private final int uid;

	/**
	 * The player's username.
	 */
	private final String username;

	/**
	 * The hash of the player's username.
	 */
	private final int usernameHash;

	/**
	 * Creates a new {@link PlayerCredentials} object with the specified name, password and uid.
	 * 
	 * @param username The player's username.
	 * @param password The player's password.
	 * @param usernameHash The hash of the player's username.
	 * @param uid The computer's uid.
	 */
	public PlayerCredentials(String username, String password, int usernameHash, int uid) {
		this.username = username;
		encodedUsername = NameUtil.encodeBase37(username);
		this.password = password;
		this.usernameHash = usernameHash;
		this.uid = uid;
	}

	/**
	 * Gets the player's username encoded as a long.
	 * 
	 * @return The username as encoded by {@link NameUtil#encodeBase37(String)}.
	 */
	public long getEncodedUsername() {
		return encodedUsername;
	}

	/**
	 * Gets the crypted password
	 * @return The password (either the original loaded from file or scrypted) 
	 */
	public String getCryptedPassword() {
		return password.startsWith("$s0$") ? password : SCryptUtil.scrypt(password, 16384, 8, 1);
	}
	
	/**
	 * Sets the player's password
	 * 
	 * @param password The player's new password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets the player's password.
	 * 
	 * @return The player's password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Gets the computer's uid.
	 * 
	 * @return The computer's uid.
	 */
	public int getUid() {
		return uid;
	}

	/**
	 * Gets the player's username.
	 * 
	 * @return The player's username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the username hash.
	 * 
	 * @return The username hash.
	 */
	public int getUsernameHash() {
		return usernameHash;
	}

}