package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inter.bank.BankDepositEnterAmountListener;
import org.apollo.game.model.inter.bank.BankUtils;
import org.apollo.game.model.inter.bank.BankWithdrawEnterAmountListener;

/**
 * A {@link MessageHandler} that handles withdrawing and depositing items from/to a player's bank.
 * 
 * @author Graham
 */
public final class BankMessageHandler extends MessageHandler<ItemActionMessage> {

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
	 * @param ctx The message handler context.
	 * @param player The player.
	 * @param message The message.
	 */
	private void deposit(MessageHandlerContext ctx, Player player, ItemActionMessage message) {
		int amount = optionToAmount(message.getOption());
		if (amount == -1) {
			player.getInterfaceSet().openEnterAmountDialogue(
					new BankDepositEnterAmountListener(player, message.getSlot(), message.getId()));
		} else if (!BankUtils.deposit(player, message.getSlot(), message.getId(), amount)) {
			ctx.breakHandlerChain();
		}
	}

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ItemActionMessage message) {
		if (player.getInterfaceSet().contains(BankConstants.BANK_WINDOW_ID)) {
			if (message.getInterfaceId() == BankConstants.SIDEBAR_INVENTORY_ID) {
				deposit(ctx, player, message);
			} else if (message.getInterfaceId() == BankConstants.BANK_INVENTORY_ID) {
				withdraw(ctx, player, message);
			}
		}
	}

	/**
	 * Handles a withdraw action.
	 * 
	 * @param ctx The message handler context.
	 * @param player The player.
	 * @param message The message.
	 */
	private void withdraw(MessageHandlerContext ctx, Player player, ItemActionMessage message) {
		int amount = optionToAmount(message.getOption());
		if (amount == -1) {
			player.getInterfaceSet().openEnterAmountDialogue(
					new BankWithdrawEnterAmountListener(player, message.getSlot(), message.getId()));
		} else if (!BankUtils.withdraw(player, message.getSlot(), message.getId(), amount)) {
			ctx.breakHandlerChain();
		}
	}

}