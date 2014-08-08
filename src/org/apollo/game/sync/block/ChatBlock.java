package org.apollo.game.sync.block;

import org.apollo.game.message.impl.ChatMessage;
import org.apollo.game.model.setting.PrivilegeLevel;

/**
 * The chat {@link SynchronizationBlock}. Only players can utilise this block.
 * 
 * @author Graham
 */
public final class ChatBlock extends SynchronizationBlock {

	/**
	 * The {@link ChatMessage}.
	 */
	private final ChatMessage chatMessage;

	/**
	 * The {@link PrivilegeLevel}.
	 */
	private final PrivilegeLevel privilegeLevel;

	/**
	 * Creates the chat block.
	 * 
	 * @param privilegeLevel The {@link PrivilegeLevel} of the player who said the message.
	 * @param chatMessage The {@link ChatMessage}.
	 */
	ChatBlock(PrivilegeLevel privilegeLevel, ChatMessage chatMessage) {
		this.privilegeLevel = privilegeLevel;
		this.chatMessage = chatMessage;
	}

	/**
	 * Gets the compressed message.
	 * 
	 * @return The compressed message.
	 */
	public byte[] getCompressedMessage() {
		return chatMessage.getCompressedMessage();
	}

	/**
	 * Gets the message.
	 * 
	 * @return The message.
	 */
	public String getMessage() {
		return chatMessage.getMessage();
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
		return chatMessage.getTextColor();
	}

	/**
	 * Gets the text effects.
	 * 
	 * @return The text effects.
	 */
	public int getTextEffects() {
		return chatMessage.getTextEffects();
	}

}