package org.apollo.game.model.inv;

import org.apollo.game.model.Item;
import org.apollo.game.model.entity.Player;

/**
 * An {@link InventoryListener} which updates the player's appearance when any items are updated.
 *
 * @author Graham
 */
public final class AppearanceInventoryListener extends InventoryAdapter {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the appearance inventory listener.
	 *
	 * @param player The player.
	 */
	public AppearanceInventoryListener(Player player) {
		this.player = player;
	}

	@Override
	public void itemsUpdated(Inventory inventory) {
		update();
	}

	@Override
	public void itemUpdated(Inventory inventory, int slot, Item item) {
		update();
	}

	/**
	 * Updates the player's appearance.
	 */
	private void update() {
		player.updateAppearance();
	}

}