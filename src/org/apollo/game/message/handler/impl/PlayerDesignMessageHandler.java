package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.game.message.impl.PlayerDesignMessage;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that handles {@link PlayerDesignMessage}s.
 * 
 * @author Graham
 */
public final class PlayerDesignMessageHandler extends MessageHandler<PlayerDesignMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, PlayerDesignMessage message) {
		player.setAppearance(message.getAppearance());
		player.setNew(true);
		player.send(new CloseInterfaceMessage());
	}

}