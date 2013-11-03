package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} which represents some sort of action on an item.
 * 
 * @author Chris Fletcher
 */
public abstract class ItemActionEvent extends InventoryItemEvent {

	/**
	 * Creates the item action event.
	 * 
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public ItemActionEvent(int option, int interfaceId, int id, int slot) {
		super(option, interfaceId, id, slot);
	}

}