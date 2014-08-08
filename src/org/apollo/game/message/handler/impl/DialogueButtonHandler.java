package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.InterfaceType;

/**
 * A {@link MessageHandler} which intercepts button clicks on dialogues, and forwards the message to the current
 * listener.
 * 
 * @author Chris Fletcher
 */
public final class DialogueButtonHandler extends MessageHandler<ButtonMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ButtonMessage message) {
		if (player.getInterfaceSet().contains(InterfaceType.DIALOGUE)) {
			boolean breakChain = player.getInterfaceSet().buttonClicked(message.getWidgetId());

			if (breakChain) {
				ctx.breakHandlerChain();
			}
		}
	}

}