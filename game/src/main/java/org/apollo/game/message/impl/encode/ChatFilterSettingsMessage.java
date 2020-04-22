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
	private final PrivacyState tradePrivacy;

	/**
	 * Creates a privacy option message.
	 *
	 * @param chatPrivacy  The privacy state of the player's chat.
	 * @param tradePrivacy The privacy state of the player's trade chat.
	 */
	public ChatFilterSettingsMessage(int chatPrivacy, int tradePrivacy) {
		this.chatPrivacy = PrivacyState.valueOf(chatPrivacy, true);
		this.tradePrivacy = PrivacyState.valueOf(tradePrivacy, false);
	}

	/**
	 * Creates a privacy option message.
	 *
	 * @param chatPrivacy  The privacy state of the player's chat.
	 * @param tradePrivacy The privacy state of the player's trade chat.
	 */
	public ChatFilterSettingsMessage(PrivacyState chatPrivacy, PrivacyState tradePrivacy) {
		this.chatPrivacy = chatPrivacy;
		this.tradePrivacy = tradePrivacy;
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
	 * Gets the trade {@link PrivacyState}.
	 *
	 * @return The privacy state.
	 */
	public PrivacyState getTradePrivacy() {
		return tradePrivacy;
	}
}
