package org.apollo.game.sync.block;

import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.setting.PrivilegeLevel;

/**
 * The chat {@link SynchronizationBlock}. Only players can utilise this block.
 * 
 * @author Graham
 */
public final class ChatBlock extends SynchronizationBlock {

	/**
	 * The {@link ChatEvent}.
	 */
	private final ChatEvent chatEvent;

	/**
	 * The {@link PrivilegeLevel}.
	 */
	private final PrivilegeLevel privilegeLevel;

	/**
	 * Creates the chat block.
	 * 
	 * @param privilegeLevel The {@link PrivilegeLevel} of the player who said the message.
	 * @param chatEvent The {@link ChatEvent}.
	 */
	ChatBlock(PrivilegeLevel privilegeLevel, ChatEvent chatEvent) {
		this.privilegeLevel = privilegeLevel;
		this.chatEvent = chatEvent;
	}

	/**
	 * Gets the compressed message.
	 * 
	 * @return The compressed message.
	 */
	public byte[] getCompressedMessage() {
		return chatEvent.getCompressedMessage();
	}

	/**
	 * Gets the message.
	 * 
	 * @return The message.
	 */
	public String getMessage() {
		return chatEvent.getMessage();
	}

	/**
	 * Gets the {@link PrivilegeLevel} of the player who said the message.
	 * 
	 * @return The privilege level.
	 */
	public PrivilegeLevel getPrivilegeLevel() {
		return privilegeLevel;
	}

	/**
	 * Gets the text color.
	 * 
	 * @return The text color.
	 */
	public int getTextColor() {
		return chatEvent.getTextColor();
	}

	/**
	 * Gets the text effects.
	 * 
	 * @return The text effects.
	 */
	public int getTextEffects() {
		return chatEvent.getTextEffects();
	}

}