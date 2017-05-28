package org.apollo.game.model.inv;

import org.apollo.game.message.impl.UpdateItemsMessage;
import org.apollo.game.message.impl.UpdateSlottedItemsMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;

/**
 * An {@link InventoryListener} which synchronizes the state of the server's inventory with the client's.
 *
 * @author Graham
 */
public final class SynchronizationInventoryListener extends InventoryAdapter {

	/**
	 * The equipment interface id.
	 */
	public static final int EQUIPMENT_ID = 1688;

	/**
	 * The inventory interface id.
	 */
	public static final int INVENTORY_ID = 3214;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the synchronization inventory listener.
	 *
	 * @param player The player.
	 * @param interfaceId The interface id.
	 */
	public SynchronizationInventoryListener(Player player, int interfaceId) {
		this.player = player;
		this.interfaceId = interfaceId;
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		player.send(new UpdateItemsMessage(interfaceId, inventory.getItems()));
	}

	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		player.send(new UpdateSlottedItemsMessage(interfaceId, new SlottedItem(slot, item)));
	}

}