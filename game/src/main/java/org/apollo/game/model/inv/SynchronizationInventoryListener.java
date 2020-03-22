package org.apollo.game.model.inv;

import org.apollo.game.message.impl.UpdateInventoryFullMessage;
import org.apollo.game.message.impl.UpdateInventoryPartialMessage;
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
	public static final int EQUIPMENT_ID = 387;
	public static final int EQUIPMENT_CONTAINER_COMPONENT = -1;
	public static final int EQUIPMENT_CONTAINER = 94;

	/**
	 * The inventory interface id.
	 */
	public static final int INVENTORY_ID = 149;
	public static final int INVENTORY_CONTAINER_COMPONENT = 0;
	public static final int INVENTORY_INVENTORY = 93;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The interface id.
	 */
	private final int component;

	/**
	 * The interface id.
	 */
	private final int inventory;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the synchronization inventory listener.
	 *
	 * @param player      The player.
	 * @param interfaceId The interface id.
	 */
	public SynchronizationInventoryListener(Player player, int interfaceId, int component, int inventory) {
		this.player = player;
		this.interfaceId = interfaceId;
		this.component = component;
		this.inventory = inventory;
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		player.send(new UpdateInventoryFullMessage(interfaceId, component, this.inventory, inventory.getItems()));
	}

	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		player.send(
				new UpdateInventoryPartialMessage(interfaceId, component, this.inventory, new SlottedItem(slot, item)));
	}

}