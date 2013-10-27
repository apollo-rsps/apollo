package org.apollo.game.model.inv;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;

/**
 * An adapter for the {@link InventoryListener}.
 * @author Graham
 */
public abstract class InventoryAdapter implements InventoryListener {

	@Override
	public void capacityExceeded(Inventory inventory) {
		/* empty */
	}

	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		/* empty */
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		/* empty */
	}

}
