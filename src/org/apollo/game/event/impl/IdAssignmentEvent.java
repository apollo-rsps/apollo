package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event which specifies the local id and membership status of the current
 * player.
 * @author Graham
 */
public final class IdAssignmentEvent extends Event {

	/**
	 * The id of this player.
	 */
	private final int id;

	/**
	 * The membership flag.
	 */
	private final boolean members;

	/**
	 * Creates the local id event.
	 * @param id The id.
	 * @param members The membership flag.
	 */
	public IdAssignmentEvent(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the membership flag.
	 * @return The membership flag.
	 */
	public boolean isMembers() {
		return members;
	}

}
