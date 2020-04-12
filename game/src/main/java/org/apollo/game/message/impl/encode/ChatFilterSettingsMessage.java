package org.apollo.game.message.impl.encode;

import org.apollo.game.model.entity.setting.PrivacyState;
import org.apollo.net.message.Message;

/**
 * @author Khaled Abdeljaber
 */
public class ChatFilterSettingsMessage extends Message {

	/**
	 * The privacy state of the player's chat.
	 */
	private final PrivacyState chatPrivacy;

	/**
	 * The privacy state of the player's friend chat.
	 */
	private final PrivacyState friendPrivacy;

	/**
	 * Creates a privacy option message.
	 *
	 * @param chatPrivacy The privacy state of the player's chat.
	 * @param friendPrivacy The privacy state of the player's friend chat.
	 */
	public ChatFilterSettingsMessage(int chatPrivacy, int friendPrivacy) {
		this.chatPrivacy = PrivacyState.valueOf(chatPrivacy, true);
		this.friendPrivacy = PrivacyState.valueOf(friendPrivacy, false);
	}

	/**
	 * Creates a privacy option message.
	 *
	 * @param chatPrivacy The privacy state of the player's chat.
	 * @param friendPrivacy The privacy state of the player's friend chat.
	 */
	public ChatFilterSettingsMessage(PrivacyState chatPrivacy, PrivacyState friendPrivacy) {
		this.chatPrivacy = chatPrivacy;
		this.friendPrivacy = chatPrivacy;
	}

	/**
	 * Gets the chat {@link PrivacyState}.
	 *
	 * @return The privacy state.
	 */
	public PrivacyState getChatPrivacy() {
		return chatPrivacy;
	}

	/**
	 * Gets the friend {@link PrivacyState}.
	 *
	 * @return The privacy state.
	 */
	public PrivacyState getFriendPrivacy() {
		return friendPrivacy;
	}
}
