package org.apollo.game.message.handler;

import org.apollo.game.message.impl.ItemOnObjectMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.World;
import org.apollo.game.model.entity.Player;
import org.apollo.game.model.inter.bank.BankConstants;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.model.inv.SynchronizationInventoryListener;

/**
 * A {@link MessageHandler} that verifies {@link ItemOnObjectMessage}s.
 *
 * @author Major
 */
public final class ItemOnObjectVerificationHandler extends MessageHandler<ItemOnObjectMessage> {

	/**
	 * Creates the ItemOnObjectVerificationHandler.
	 *
	 * @param world The {@link World} the {@link ItemOnObjectMessage} occurred in.
	 */
	public ItemOnObjectVerificationHandler(World world) {
		super(world);
	}

	@Override
	public void handle(Player player, ItemOnObjectMessage message) {
		if (message.getInterfaceId() != SynchronizationInventoryListener.INVENTORY_ID && message.getInterfaceId() != BankConstants.SIDEBAR_INVENTORY_ID) {
			message.terminate();
			return;
		}

		Inventory inventory = player.getInventory();

		int slot = message.getSlot();
		if (slot < 0 || slot >= inventory.capacity()) {
			message.terminate();
			return;
		}

		Item item = inventory.get(slot);
		if (item == null || item.getId() != message.getId()) {
			message.terminate();
			return;
		}
	}

}