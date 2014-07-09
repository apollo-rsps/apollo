package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOnObjectEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An {@link EventHandler} that verifies {@link ItemObObjectEvent}s.
 * 
 * @author Major
 */
public final class ItemOnObjectVerificationHandler extends EventHandler<ItemOnObjectEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemOnObjectEvent event) {
	if (event.getInterfaceId() != SynchronizationInventoryListener.INVENTORY_ID
		&& event.getInterfaceId() != BankConstants.SIDEBAR_INVENTORY_ID) {
	    ctx.breakHandlerChain();
	    return;
	}

	Inventory inventory = player.getInventory();

	int slot = event.getSlot();
	if (slot < 0 || slot >= inventory.capacity()) {
	    ctx.breakHandlerChain();
	    return;
	}

	Item item = inventory.get(slot);
	if (item == null || item.getId() != event.getId()) {
	    ctx.breakHandlerChain();
	    return;
	}
    }

}