package org.apollo.game.event.impl;

import java.util.List;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client that updates the ignored user list.
 * 
 * @author Major
 */
public final class IgnoreListEvent extends Event {

	/**
	 * The list of ignored player usernames.
	 */
	private final List<String> usernames;

	/**
	 * Creates a new ignore list event.
	 * 
	 * @param player The player.
	 */
	public IgnoreListEvent(List<String> usernames) {
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