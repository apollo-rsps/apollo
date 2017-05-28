package org.apollo.game.message.impl;

import org.apollo.game.model.entity.setting.PrivilegeLevel;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that forwards a private chat.
 *
 * @author Major
 */
public final class ForwardPrivateChatMessage extends Message {

	/**
	 * The username of the player sending the message.
	 */
	private final String username;

	/**
	 * The privilege level of the player.
	 */
	private final PrivilegeLevel privilege;

	/**
	 * The message.
	 */
	private final byte[] message;

	/**
	 * Creates a new forward private message message.
	 *
	 * @param username The username of the player sending the message.
	 * @param level The {@link PrivilegeLevel} of the player sending the message.
	 * @param message The compressed message.
	 */
	public ForwardPrivateChatMessage(String username, PrivilegeLevel level, byte[] message) {
		this.username = username;
		privilege = level;
		this.message = message;
	}

	/**
	 * Gets the username of the sender.
	 *
	 * @return The username.
	 */
	public String getSenderUsername() {
		return username;
	}

	/**
	 * Gets the {@link PrivilegeLevel} of the sender.
	 *
	 * @return The privilege level.
	 */
	public PrivilegeLevel getSenderPrivilege() {
		return privilege;
	}

	/**
	 * Gets the compressed message.
	 *
	 * @return The message.
	 */
	public byte[] getCompressedMessage() {
		return message;
	}

}