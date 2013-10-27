package org.apollo.game.event.handler.impl;

import org.apollo.game.event.handler.EventHandler;
import org.apollo.game.event.handler.EventHandlerContext;
import org.apollo.game.event.impl.SwitchItemEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * An {@link EventHandler} which updates an {@link Inventory} when the client
 * sends a {@link SwitchItemEvent} to the server.
 * @author Graham
 */
public final class SwitchItemEventHandler extends EventHandler<SwitchItemEvent> {

	@Override
	public void handle(EventHandlerContext ctx, Player player, SwitchItemEvent event) {
		Inventory inventory;
		boolean insertPermitted = false;

		// TODO is there a better way of doing this??
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
			insertPermitted = true;
			break;
		default:
			return; // not a known inventory, ignore
		}

		if (event.getOldSlot() >= 0 && event.getNewSlot() >= 0 && event.getOldSlot() < inventory.capacity() && event.getNewSlot() < inventory.capacity()) {
			// events must be fired for it to work if a sidebar inv overlay is used
			inventory.swap(insertPermitted ? event.isInserting() : false, event.getOldSlot(), event.getNewSlot());
		}
	}

}
