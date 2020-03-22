package org.apollo.game.message.impl;

import org.apollo.game.model.inv.SlottedItem;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that updates a single item in an interface.
 *
 * @author Graham
 */
public final class UpdateInventoryPartialMessage extends Message {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The component.
	 */
	private final int component;

	/**
	 * The id of this container.
	 */
	private final int containerId;

	/**
	 * The slotted items.
	 */
	private final SlottedItem[] items;

	/**
	 * Creates the update item in interface message.
	 *
	 * @param interfaceId The interface id.
	 * @param items       The slotted items.
	 */
	public UpdateInventoryPartialMessage(int interfaceId, int containerId, int component, SlottedItem... items) {
		this.interfaceId = interfaceId;
		this.component = component;
		this.containerId = containerId;
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
	public int getContainerId() {
		return containerId;
	}

	/**
	 * Gets an array of slotted items.
	 *
	 * @return The slotted items.
	 */
	public SlottedItem[] getSlottedItems() {
		return items;
	}

}