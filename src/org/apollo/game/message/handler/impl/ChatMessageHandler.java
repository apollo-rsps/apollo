package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ChatMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * A {@link MessageHandler} that broadcasts public chat messages.
 * 
 * @author Graham
 */
public final class ChatMessageHandler extends MessageHandler<ChatMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ChatMessage message) {
		player.getBlockSet().add(SynchronizationBlock.createChatBlock(player, message));
	}

}