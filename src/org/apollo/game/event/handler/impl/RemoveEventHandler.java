package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.ItemActionEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An event handler which removes equipped items.
 * @author Graham
 */
public final class RemoveEventHandler extends EventHandler<ItemActionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, ItemActionEvent event) {
		if (event.getOption() == 1 && event.getInterfaceId() == SynchronizationInventoryListener.EQUIPMENT_ID) {
			Inventory inventory = player.getInventory();
			Inventory equipment = player.getEquipment();

			int slot = event.getSlot();
			if (slot < 0 || slot >= equipment.capacity()) {
				ctx.breakHandlerChain();
				return;
			}

			Item item = equipment.get(slot);
			if (item == null || item.getId() != event.getId()) {
				ctx.breakHandlerChain();
				return;
			}

			boolean removed = true;

			inventory.stopFiringEvents();
			equipment.stopFiringEvents();

			try {
				equipment.set(slot, null);
				Item tmp = inventory.add(item);
				if (tmp != null) {
					removed = false;
					equipment.set(slot, tmp);
				}
			} finally {
				inventory.startFiringEvents();
				equipment.startFiringEvents();
			}

			if (removed) {
				inventory.forceRefresh(); // TODO find out the specific slot that got used?
				equipment.forceRefresh(slot);
			} else {
				inventory.forceCapacityExceeded();
			}
		}
	}

}
