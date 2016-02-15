package org.apollo.game.model.inv;

import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.game.model.entity.Player;
import org.apollo.net.message.Message;

/**
 * An {@link InventoryListener} which sends a message to a player when an inventory has run out of space.
 *
 * @author Graham
 */
public final class FullInventoryListener extends InventoryAdapter {

	/**
	 * The bank full message.
	 */
	public static final String FULL_BANK_MESSAGE = "You could not bank all your items.";

	/**
	 * The inventory full message.
	 */
	public static final String FULL_INVENTORY_MESSAGE = "You don't have enough inventory space.";

	/**
	 * The message to send when the capacity has been exceeded.
	 */
	private final Message message;

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * Creates the empty inventory listener.
	 *
	 * @param player The player.
	 * @param message The message to send when the inventory is empty.
	 */
	public FullInventoryListener(Player player, String message) {
		this.player = player;
		this.message = new ServerChatMessage(message);
	}

	@Override
	public void capacityExceeded(Inventory inventory) {
		player.send(message);
	}

}