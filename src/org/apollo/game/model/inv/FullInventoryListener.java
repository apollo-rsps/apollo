package org.apollo.game.model.inv;

import org.apollo.game.event.Event;
import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.game.model.Inventory;
import org.apollo.game.model.Player;

/**
 * An {@link InventoryListener} which sends a message to a player when an
 * inventory has run out of space.
 * @author Graham
 */
public final class FullInventoryListener extends InventoryAdapter {

	/**
	 * The inventory full message.
	 */
	public static final String FULL_INVENTORY_MESSAGE = "Not enough inventory space.";

	/**
	 * The bank full message.
	 */
	public static final String FULL_BANK_MESSAGE = "Not enough bank space.";

	/**
	 * The equipment full message.
	 */
	public static final String FULL_EQUIPMENT_MESSAGE = "Not enough equipment space."; // TODO confirm if possible

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The event to send when the capacity has been exceeded.
	 */
	private final Event event;

	/**
	 * Creates the empty inventory listener.
	 * @param player The player.
	 * @param message The message to send when the inventory is empty.
	 */
	public FullInventoryListener(Player player, String message) {
		this.player = player;
		this.event = new ServerMessageEvent(message);
	}

	@Override
	public void capacityExceeded(Inventory inventory) {
		player.send(event);
	}

}
