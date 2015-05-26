package org.apollo.game.message.handler;

import org.apollo.game.message.impl.SwitchItemMessage;
import org.apollo.game.model.World;
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

	/**
	 * Creates the SwitchItemMessageHandler.
	 *
	 * @param world The {@link World} the {@link SwitchItemMessage} occurred in.
	 */
	public SwitchItemMessageHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, SwitchItemMessage message) {
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

		int old = message.getOldSlot(), next = message.getNewSlot();
		if (old >= 0 && next >= 0 && old < inventory.capacity() && next < inventory.capacity()) {
			// events must be fired for it to work if a sidebar inventory overlay is used
			inventory.swap(insertPermitted && message.isInserting(), old, next);
		}
	}

}