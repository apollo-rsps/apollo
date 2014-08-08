package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.game.model.entity.Player;

/**
 * A {@link MessageHandler} that responds to {@link ButtonMessage}s for withdrawing items as notes.
 * 
 * @author Graham
 */
public final class BankButtonMessageHandler extends MessageHandler<ButtonMessage> {

	/**
	 * The withdraw as item button id.
	 */
	private static final int WITHDRAW_AS_ITEM = 5387;

	/**
	 * The withdraw as note button id.
	 */
	private static final int WITHDRAW_AS_NOTE = 5386;

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ButtonMessage message) {
		if (message.getWidgetId() == WITHDRAW_AS_ITEM) {
			player.setWithdrawingNotes(false);
		} else if (message.getWidgetId() == WITHDRAW_AS_NOTE) {
			player.setWithdrawingNotes(true);
		}
	}

}