package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.InventoryItemMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that verifies {@link InventoryItemMessage}s.
 * 
 * @author Chris Fletcher
 */
public final class ItemVerificationHandler extends MessageHandler<InventoryItemMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, InventoryItemMessage message) {
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

		int slot = message.getSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getId()) {
			ctx.breakHandlerChain();
		}
	}

}