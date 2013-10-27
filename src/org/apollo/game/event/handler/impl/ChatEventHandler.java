package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ChatEvent;
import org.apollo.game.model.Player;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * An event handler which broadcasts public chat messages.
 * @author Graham
 */
public final class ChatEventHandler extends EventHandler<ChatEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ChatEvent event) {
		player.getBlockSet().add(SynchronizationBlock.createChatBlock(player, event));
	}

}
