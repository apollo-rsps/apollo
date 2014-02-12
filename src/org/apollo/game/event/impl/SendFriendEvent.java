package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to add a player to the friend list.
 * 
 * @author Major
 */
public final class SendFriendEvent extends Event {

	/**
	 * The username of the friend.
	 */
	private final String username;

	/**
	 * The world id the friend is in.
	 */
	private final int world;

	/**
	 * Creates a new send friend event.
	 * 
	 * @param username The username of the friend.
	 * @param world The world the friend is in.
	 */
	public SendFriendEvent(String username, int world) {
		this.username = username;
		this.world = world == 0 ? 0 : world + 9;
	}

	/**
	 * Gets the username of the friend.
	 * 
	 * @return The username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Gets the world id the friend is in.
	 * 
	 * @return The world id.
	 */
	public int getWorld() {
		return world;
	}

}