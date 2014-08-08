package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ChatMessage;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that verifies {@link ChatMessage}s.
 * 
 * @author Graham
 */
public final class ChatVerificationHandler extends MessageHandler<ChatMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ChatMessage message) {
		int color = message.getTextColor();
		int effects = message.getTextEffects();
		if (color < 0 || color > 11 || effects < 0 || effects > 5) {
			ctx.breakHandlerChain();
		}
	}

}