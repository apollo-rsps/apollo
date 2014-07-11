package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inter.bank.BankDepositEnterAmountListener;
import org.apollo.game.model.inter.bank.BankUtils;
import org.apollo.game.model.inter.bank.BankWithdrawEnterAmountListener;

/**
 * An {@link EventHandler} that handles withdrawing and depositing items from/to a player's bank.
 * 
 * @author Graham
 */
public final class BankEventHandler extends EventHandler<ItemActionEvent> {

	/**
	 * Converts an option to an amount.
	 * 
	 * @param option The option.
	 * @return The amount.
	 * @throws IllegalArgumentException If the option is invalid.
	 */
	private static final int optionToAmount(int option) {
		switch (option) {
		case 1:
			return 1;
		case 2:
			return 5;
		case 3:
			return 10;
		case 4:
			return Integer.MAX_VALUE;
		case 5:
			return -1;
		}
		throw new IllegalArgumentException("Invalid option supplied.");
	}

	/**
	 * Handles a deposit action.
	 * 
	 * @param ctx The event handler context.
	 * @param player The player.
	 * @param event The event.
	 */
	private void deposit(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		int amount = optionToAmount(event.getOption());
		if (amount == -1) {
			player.getInterfaceSet().openEnterAmountDialogue(
					new BankDepositEnterAmountListener(player, event.getSlot(), event.getId()));
		} else if (!BankUtils.deposit(player, event.getSlot(), event.getId(), amount)) {
			ctx.breakHandlerChain();
		}
	}

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		if (player.getInterfaceSet().contains(BankConstants.BANK_WINDOW_ID)) {
			if (event.getInterfaceId() == BankConstants.SIDEBAR_INVENTORY_ID) {
				deposit(ctx, player, event);
			} else if (event.getInterfaceId() == BankConstants.BANK_INVENTORY_ID) {
				withdraw(ctx, player, event);
			}
		}
	}

	/**
	 * Handles a withdraw action.
	 * 
	 * @param ctx The event handler context.
	 * @param player The player.
	 * @param event The event.
	 */
	private void withdraw(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		int amount = optionToAmount(event.getOption());
		if (amount == -1) {
			player.getInterfaceSet().openEnterAmountDialogue(
					new BankWithdrawEnterAmountListener(player, event.getSlot(), event.getId()));
		} else if (!BankUtils.withdraw(player, event.getSlot(), event.getId(), amount)) {
			ctx.breakHandlerChain();
		}
	}

}