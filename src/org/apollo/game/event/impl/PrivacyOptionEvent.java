package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.PrivacyState;

/**
 * An {@link Event} sent by the client or server to update the chat and trade privacy state.
 * 
 * @author Kyle Stevenson
 */
public class PrivacyOptionEvent extends Event {

	/**
	 * The privacy state of the player's public chat.
	 */
	private final PrivacyState publicChatState;

	/**
	 * The privacy state of the player's private chat.
	 */
	private final PrivacyState privateChatState;

	/**
	 * The privacy state of the player's trade chat.
	 */
	private final PrivacyState tradeChatState;

	/**
	 * Creates a privacy option event.
	 * 
	 * @param publicChatState The privacy state of the player's public chat.
	 * @param privateChatState The privacy state of the player's private chat.
	 * @param tradeChatState The privacy state of the player's trade chat.
	 */
	public PrivacyOptionEvent(int publicChatState, int privateChatState, int tradeChatState) {
		this.publicChatState = PrivacyState.valueOf(publicChatState);
		this.privateChatState = PrivacyState.valueOf(privateChatState);
		this.tradeChatState = PrivacyState.valueOf(tradeChatState);
	}

	/**
	 * Gets the public chat {@link PrivacyState}.
	 * 
	 * @return The privacy option.
	 */
	public PrivacyState getPublicChatPrivacy() {
		return publicChatState;
	}

	/**
	 * Gets the private chat {@link PrivacyState}.
	 * 
	 * @return The privacy option.
	 */
	public PrivacyState getPrivateChatPrivacy() {
		return privateChatState;
	}

	/**
	 * Gets the trade chat {@link PrivacyState}.
	 * 
	 * @return The privacy option.
	 */
	public PrivacyState getTradeChatPrivacy() {
		return tradeChatState;
	}

}