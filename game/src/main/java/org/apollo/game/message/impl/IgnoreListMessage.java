package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

import java.util.List;

/**
 * A {@link Message} sent to the client that updates the ignored user list.
 *
 * @author Major
 */
public final class IgnoreListMessage extends Message {

	/**
	 * The list of ignored player usernames.
	 */
	private final List<String> usernames;

	/**
	 * Creates a new ignore list message.
	 *
	 * @param usernames The {@link List} of usernames to send.
	 */
	public IgnoreListMessage(List<String> usernames) {
		this.usernames = usernames;
	}

	/**
	 * Gets the list of ignored usernames.
	 *
	 * @return The usernames.
	 */
	public List<String> getUsernames() {
		return usernames;
	}

}