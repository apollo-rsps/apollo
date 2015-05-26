package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.EnterAmountListener;
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
	private static int optionToAmount(int option) {
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
	 * Creates the BankMessageHandler.
	 *
	 * @param world The {@link World} the {@link ItemActionMessage} occurred in.
	 */
	public BankMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ItemActionMessage message) {
		if (player.getInterfaceSet().contains(BankConstants.BANK_WINDOW_ID)) {
			if (message.getInterfaceId() == BankConstants.SIDEBAR_INVENTORY_ID) {
				deposit(player, message);
			} else if (message.getInterfaceId() == BankConstants.BANK_INVENTORY_ID) {
				withdraw(player, message);
			}
		}
	}

	/**
	 * Handles a deposit action.
	 *
	 * @param player The player.
	 * @param message The message.
	 */
	private void deposit(Player player, ItemActionMessage message) {
		int amount = optionToAmount(message.getOption());

		if (amount == -1) {
			EnterAmountListener listener = new BankDepositEnterAmountListener(player, message.getSlot(), message.getId());
			player.getInterfaceSet().openEnterAmountDialogue(listener);
		} else if (!BankUtils.deposit(player, message.getSlot(), message.getId(), amount)) {
			message.terminate();
		}
	}

	/**
	 * Handles a withdraw action.
	 *
	 * @param player The player.
	 * @param message The message.
	 */
	private void withdraw(Player player, ItemActionMessage message) {
		int amount = optionToAmount(message.getOption());

		if (amount == -1) {
			EnterAmountListener listener = new BankWithdrawEnterAmountListener(player, message.getSlot(), message.getId());
			player.getInterfaceSet().openEnterAmountDialogue(listener);
		} else if (!BankUtils.withdraw(player, message.getSlot(), message.getId(), amount)) {
			message.terminate();
		}
	}

}