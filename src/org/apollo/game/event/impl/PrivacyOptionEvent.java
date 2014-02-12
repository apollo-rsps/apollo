package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.settings.PrivacyState;

/**
 * An {@link Event} sent by the client or server to update the chat and trade privacy state.
 * 
 * @author Kyle Stevenson
 * @author Major
 */
public final class PrivacyOptionEvent extends Event {

	/**
	 * The privacy state of the player's chat.
	 */
	private final PrivacyState chatPrivacy;

	/**
	 * The privacy state of the player's friend chat.
	 */
	private final PrivacyState friendPrivacy;

	/**
	 * The privacy state of the player's trade chat.
	 */
	private final PrivacyState tradePrivacy;

	/**
	 * Creates a privacy option event.
	 * 
	 * @param chatPrivacy The privacy state of the player's chat.
	 * @param friendPrivacy The privacy state of the player's friend chat.
	 * @param tradePrivacy The privacy state of the player's trade chat.
	 */
	public PrivacyOptionEvent(int chatPrivacy, int friendPrivacy, int tradePrivacy) {
		this.chatPrivacy = PrivacyState.valueOf(chatPrivacy);
		this.friendPrivacy = PrivacyState.valueOf(friendPrivacy);
		this.tradePrivacy = PrivacyState.valueOf(tradePrivacy);
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

	/**
	 * Gets the trade {@link PrivacyState}.
	 * 
	 * @return The privacy state.
	 */
	public PrivacyState getTradePrivacy() {
		return tradePrivacy;
	}

}