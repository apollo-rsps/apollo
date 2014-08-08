package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ItemOnItemMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that verifies the target item in {@link ItemOnItemMessage}s.
 * 
 * @author Chris Fletcher
 */
public final class ItemOnItemVerificationHandler extends MessageHandler<ItemOnItemMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ItemOnItemMessage message) {
		Inventory inventory;

		switch (message.getInterfaceId()) {
		case SynchronizationInventoryListener.INVENTORY_ID:
		case BankConstants.SIDEBAR_INVENTORY_ID:
			inventory = player.getInventory();
			break;
		case SynchronizationInventoryListener.EQUIPMENT_ID:
			inventory = player.getEquipment();
			break;
		case BankConstants.BANK_INVENTORY_ID:
			inventory = player.getBank();
			break;
		default:
			ctx.breakHandlerChain();
			return;
		}

		int slot = message.getTargetSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getTargetId()) {
			ctx.breakHandlerChain();
		}
	}

}