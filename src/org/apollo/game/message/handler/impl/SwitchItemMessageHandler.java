package org.apollo.game.message.handler.impl;

import org.apollo.game.message.handler.MessageHandler;
import org.apollo.game.message.handler.MessageHandlerContext;
import org.apollo.game.message.impl.SwitchItemMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} which updates an {@link Inventory} when the client sends a {@link SwitchItemMessage} to the
 * server.
 * 
 * @author Graham
 */
public final class SwitchItemMessageHandler extends MessageHandler<SwitchItemMessage> {

	@Override
	public void handle(MessageHandlerContext ctx, Player player, SwitchItemMessage message) {
		Inventory inventory;
		boolean insertPermitted = false;

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
			insertPermitted = true;
			break;
		default:
			return; // not a known inventory, ignore
		}

		if (message.getOldSlot() >= 0 && message.getNewSlot() >= 0 && message.getOldSlot() < inventory.capacity()
				&& message.getNewSlot() < inventory.capacity()) {
			// events must be fired for it to work if a sidebar inventory overlay is used
			inventory.swap(insertPermitted && message.isInserting(), message.getOldSlot(), message.getNewSlot());
		}
	}

}