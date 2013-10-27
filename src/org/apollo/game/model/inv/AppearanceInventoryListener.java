package org.apollo.game.model.inv;

import org.apollo.game.model.Inventory;
import org.apollo.game.model.Item;
import org.apollo.game.model.Player;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * An {@link InventoryListener} which updates the player's appearance when
 * any items are updated.
 * @author Graham
 */
public final class AppearanceInventoryListener extends InventoryAdapter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the appearance inventory listener.
	 * @param player The player.
	 */
	public AppearanceInventoryListener(Player player) {
		this.player = player;
	}

	/**
	 * Updates the player's appearance.
	 */
	private void update() {
		player.getBlockSet().add(SynchronizationBlock.createAppearanceBlock(player));
	}

	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		update();
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		update();
	}

}
