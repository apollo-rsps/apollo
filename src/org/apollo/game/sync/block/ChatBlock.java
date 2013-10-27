package org.apollo.game.sync.block;

import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.Player.PrivilegeLevel;

/**
 * The chat {@link SynchronizationBlock}.
 * @author Graham
 */
public final class ChatBlock extends SynchronizationBlock {

	/**
	 * The privilege level.
	 */
	private final PrivilegeLevel privilegeLevel;

	/**
	 * The chat event.
	 */
	private final ChatEvent chatEvent;

	/**
	 * Creates the chat block.
	 */
	ChatBlock(PrivilegeLevel privilegeLevel, ChatEvent chatEvent) {
		this.privilegeLevel = privilegeLevel;
		this.chatEvent = chatEvent;
	}

	/**
	 * Gets the privilege level of the player who said the message.
	 * @return The privilege level.
	 */
	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}

	/**
	 * Gets the message.
	 * @return The message.
	 */
	public String getMessage() {
		return chatEvent.getMessage();
	}

	/**
	 * Gets the text color.
	 * @return The text color.
	 */
	public int getTextColor() {
		return chatEvent.getTextColor();
	}

	/**
	 * Gets the text effects.
	 * @return The text effects.
	 */
	public int getTextEffects() {
		return chatEvent.getTextEffects();
	}

	/**
	 * Gets the compressed message.
	 * @return The compressed message.
	 */
	public byte[] getCompressedMessage() {
		return chatEvent.getCompressedMessage();
	}

}
