package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ClosedInterfaceMessage;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} for the {@link ClosedInterfaceMessage}.
 * 
 * @author Graham
 */
public final class ClosedInterfaceMessageHandler extends MessageHandler<ClosedInterfaceMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ClosedInterfaceMessage message) {
		player.getInterfaceSet().interfaceClosed();
	}

}