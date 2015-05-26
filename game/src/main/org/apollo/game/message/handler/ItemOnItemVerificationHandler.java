package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ItemOnItemMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that verifies the target item in {@link ItemOnItemMessage}s.
 *
 * @author Chris Fletcher
 */
public final class ItemOnItemVerificationHandler extends MessageHandler<ItemOnItemMessage> {

	/**
	 * Creates the ItemOnItemVerificationHandler.
	 *
	 * @param world The {@link World} the {@link ItemOnItemMessage} occurred in.
	 */
	public ItemOnItemVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ItemOnItemMessage message) {
		Inventory inventory;

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
				break;
			default:
				message.terminate();
				return;
		}

		int slot = message.getTargetSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			message.terminate();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getTargetId()) {
			message.terminate();
		}
	}

}