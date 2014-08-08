package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that removes equipped items.
 * 
 * @author Graham
 * @author Major
 */
public final class RemoveEquippedItemHandler extends MessageHandler<ItemActionMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, ItemActionMessage message) {
		if (message.getOption() == 1 && message.getInterfaceId() == SynchronizationInventoryListener.EQUIPMENT_ID) {
			Inventory inventory = player.getInventory();
			Inventory equipment = player.getEquipment();

			int slot = message.getSlot();
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