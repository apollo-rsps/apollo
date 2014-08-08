package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ItemOnObjectMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that verifies {@link ItemOnObjectMessage}s.
 * 
 * @author Major
 */
public final class ItemOnObjectVerificationHandler extends MessageHandler<ItemOnObjectMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ItemOnObjectMessage message) {
		if (message.getInterfaceId() != SynchronizationInventoryListener.INVENTORY_ID
				&& message.getInterfaceId() != BankConstants.SIDEBAR_INVENTORY_ID) {
			ctx.breakHandlerChain();
			return;
		}

		Inventory inventory = player.getInventory();

		int slot = message.getSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getId()) {
			ctx.breakHandlerChain();
			return;
		}
	}

}