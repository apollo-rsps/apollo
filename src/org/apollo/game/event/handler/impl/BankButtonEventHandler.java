package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ButtonEvent;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} which responds to {@link ButtonEvent}s for
 * withdrawing items as notes.
 * @author Graham
 */
public final class BankButtonEventHandler extends EventHandler<ButtonEvent> {

	/**
	 * The withdraw as item button id.
	 */
	private static final int WITHDRAW_AS_ITEM = 5387;

	/**
	 * The withdraw as note button id.
	 */
	private static final int WITHDRAW_AS_NOTE = 5386;

	@Override
	public void handle(EventHandlerContext ctx, Player player, ButtonEvent event) {
		if (event.getInterfaceId() == WITHDRAW_AS_ITEM) {
			player.setWithdrawingNotes(false);
		} else if (event.getInterfaceId() == WITHDRAW_AS_NOTE) {
			player.setWithdrawingNotes(true);
		}
	}

}
