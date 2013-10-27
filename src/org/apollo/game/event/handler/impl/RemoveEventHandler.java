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
 * 
 * @author Graham
 */
public final class RemoveEventHandler extends EventHandler<ItemActionEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player,
			ItemActionEvent event) {

		if (event.getOption() == 1
				&& event.getInterfaceId() == SynchronizationInventoryListener.EQUIPMENT_ID) {
			Inventory inventory = player.getInventory();
			Inventory equipment = player.getEquipment();

			if (inventory.freeSlots() < 1) { // TODO what if the item is
												// stackable and the player has
												// the item in his inventory
												// already.
				inventory.forceCapacityExceeded();
				ctx.breakHandlerChain();
				return;
			}

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
				Item copy = item;
				inventory.add(item.getId(), item.getAmount());
				if (copy != null) {
					removed = false;
					equipment.set(slot, copy);
				}
			} finally {
				inventory.startFiringEvents();
				equipment.startFiringEvents();
			}

			if (removed) {
				inventory.forceRefresh(); // TODO find out the specific slot
											// that got used?
				equipment.forceRefresh();
			} else {
				inventory.forceCapacityExceeded();
			}
		}
	}

}