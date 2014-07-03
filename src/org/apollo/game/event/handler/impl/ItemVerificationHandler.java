package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.InventoryItemEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An {@link EventHandler} that verifies {@link InventoryItemEvent}s.
 * 
 * @author Chris Fletcher
 */
public final class ItemVerificationHandler extends EventHandler<InventoryItemEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, InventoryItemEvent event) {
		Inventory inventory;

		switch (event.getInterfaceId()) {
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

		int slot = event.getSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != event.getId()) {
			ctx.breakHandlerChain();
		}
	}

}