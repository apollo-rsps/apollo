package org.apollo.game.message.impl.encode;

import org.apollo.game.model.entity.setting.PrivacyState;
import org.apollo.net.message.Message;

/**
 * @author Khaled Abdeljaber
 */
public class SetPrivateChatFilterMessage extends Message {

	/**
	 * The privacy state of the player's trade chat.
	 */
	private final PrivacyState friendPrivacy;

	/**
	 * Creates a privacy option message.
	 *
	 * @param friendPrivacy The privacy state of the player's friend chat.
	 */
	public SetPrivateChatFilterMessage(PrivacyState friendPrivacy) {
		this.friendPrivacy = friendPrivacy;
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
