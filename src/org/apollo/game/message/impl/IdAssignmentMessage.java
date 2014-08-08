package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent to the client that specifies the local id and membership status of the current player.
 * 
 * @author Graham
 */
public final class IdAssignmentMessage extends Message {

	/**
	 * The id of this player.
	 */
	private final int id;

	/**
	 * The membership flag.
	 */
	private final boolean members;

	/**
	 * Creates the local id message.
	 * 
	 * @param id The id.
	 * @param members The membership flag.
	 */
	public IdAssignmentMessage(int id, boolean members) {
		this.id = id;
		this.members = members;
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the membership flag.
	 * 
	 * @return The membership flag.
	 */
	public boolean isMembers() {
		return members;
	}

}