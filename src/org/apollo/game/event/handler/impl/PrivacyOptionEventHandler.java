package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.PrivacyOptionEvent;
import org.apollo.game.model.Player;

/**
 * Handles {@link PrivacyOptionEvent}s from the client.
 * 
 * @author Kyle Stevenson
 */
public class PrivacyOptionEventHandler extends EventHandler<PrivacyOptionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, PrivacyOptionEvent event) {
		player.setPublicChatPrivacy(event.getPublicChatPrivacy());
		player.setPrivateChatPrivacy(event.getPrivateChatPrivacy());
		player.setTradeChatPrivacy(event.getTradeChatPrivacy());
	}

}