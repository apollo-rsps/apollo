package org.apollo.game.message.impl.decode;

import org.apollo.game.message.impl.encode.ChatFilterSettingsMessage;
import org.apollo.game.model.entity.setting.PrivacyState;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent both by and to the client to update the public chat, private (friend) chat, and trade chat
 * privacy state.
 *
 * @author Kyle Stevenson
 * @author Major
 */
public final class PrivacyOptionMessage extends ChatFilterSettingsMessage {

	/**
	 * The privacy state of the player's trade chat.
	 */
	private final PrivacyState friendPrivacy;

	/**
	 * Creates a privacy option message.
	 *
	 * @param chatPrivacy The privacy state of the player's chat.
	 * @param friendPrivacy The privacy state of the player's friend chat.
	 * @param tradePrivacy The privacy state of the player's trade chat.
	 */
	public PrivacyOptionMessage(int chatPrivacy, int friendPrivacy, int tradePrivacy) {
		super(chatPrivacy, tradePrivacy);
		this.friendPrivacy = PrivacyState.valueOf(friendPrivacy, false);
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