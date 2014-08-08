package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.EnteredAmountMessage;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} for the {@link EnteredAmountMessage}.
 * 
 * @author Graham
 */
public final class EnteredAmountMessageHandler extends MessageHandler<EnteredAmountMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, EnteredAmountMessage message) {
		player.getInterfaceSet().enteredAmount(message.getAmount());
	}

}