package org.apollo.game.message.impl.encode;

import org.apollo.game.model.Item;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that updates all the items in an interface.
 *
 * @author Graham
 */
public final class UpdateInventoryFullMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The component.
	 */
	private final int component;

	/**
	 * The id of this inventory.
	 */
	private final int inventory;

	/**
	 * The items.
	 */
	private final Item[] items;

	/**
	 * Creates the update inventory interface message.
	 *
	 * @param interfaceId The interface id.
	 * @param items The items.
	 */
	public UpdateInventoryFullMessage(int interfaceId, int component, int inventory, Item[] items) {
		this.interfaceId = interfaceId;
		this.component = component;
		this.inventory = inventory;
		this.items = items;
	}

	/**
	 * Gets the interface id.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the component.
	 *
	 * @return The component.
	 */
	public int getComponent() {
		return component;
	}

	/**
	 * Gets container id.
	 *
	 * @return the container id
	 */
	public int getInventory() {
		return inventory;
	}

	/**
	 * Gets the items.
	 *
	 * @return The items.
	 */
	public Item[] getItems() {
		return items;
	}

}