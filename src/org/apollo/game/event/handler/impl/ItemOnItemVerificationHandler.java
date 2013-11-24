package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemOnItemEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;

/**
 * An {@link EventHandler} which verifies the target item in {@link ItemOnItemEvent}s.
 * 
 * @author Chris Fletcher
 */
public final class ItemOnItemVerificationHandler extends EventHandler<ItemOnItemEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemOnItemEvent event) {
		Inventory inventory = ItemVerificationHandler.interfaceToInventory(player, event.getTargetInterfaceId());

		int slot = event.getTargetSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			ctx.breakHandlerChain();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != event.getTargetId()) {
			ctx.breakHandlerChain();
		}
	}

}