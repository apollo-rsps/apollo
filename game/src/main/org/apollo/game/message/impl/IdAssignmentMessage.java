package org.apollo.game.message.impl;

import org.apollo.game.model.entity.setting.MembershipStatus;
import org.apollo.net.message.Message;

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
	 * The MembershipStatus.
	 */
	private final MembershipStatus members;

	/**
	 * Creates the local id message.
	 *
	 * @param id The id.
	 * @param members The MembershipStatus.
	 */
	public IdAssignmentMessage(int id, MembershipStatus members) {
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
	 * Gets whether or not the Player is a {@link MembershipStatus#PAID paying member}.
	 *
	 * @return {@code true} if the Player is a paying member, {@code false} if not.
	 */
	public boolean isMembers() {
		return members == MembershipStatus.PAID;
	}

}