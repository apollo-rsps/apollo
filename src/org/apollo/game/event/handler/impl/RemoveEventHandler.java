package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An {@link EventHandler} that removes equipped items.
 * 
 * @author Graham
 * @author Major
 */
public final class RemoveEventHandler extends EventHandler<ItemActionEvent> {

    @Override
    public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
	if (event.getOption() == 1 && event.getInterfaceId() == SynchronizationInventoryListener.EQUIPMENT_ID) {
	    Inventory inventory = player.getInventory();
	    Inventory equipment = player.getEquipment();

	    int slot = event.getSlot();
	    Item item = equipment.get(slot);
	    int id = item.getId();

	    if (inventory.freeSlots() == 0 && !item.getDefinition().isStackable()) {
		inventory.forceCapacityExceeded();
		ctx.breakHandlerChain();
		return;
	    }

	    boolean removed = true;

	    inventory.stopFiringEvents();
	    equipment.stopFiringEvents();

	    try {
		int remaining = inventory.add(id, item.getAmount());
		removed = remaining == 0;
		equipment.set(slot, removed ? null : new Item(id, remaining));
	    } finally {
		inventory.startFiringEvents();
		equipment.startFiringEvents();
	    }

	    if (removed) {
		inventory.forceRefresh();
		equipment.forceRefresh(slot);
	    } else {
		inventory.forceCapacityExceeded();
	    }
	}
    }

}