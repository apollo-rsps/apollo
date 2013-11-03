package org.apollo.game.event.impl;

/**
 * An {@link InventoryItemEvent} which is sent by the client when an item's option is clicked (e.g. equip,
 * eat, drink, etc).
 * @author Chris Fletcher
 */
public abstract class ItemOptionEvent extends InventoryItemEvent {

	/**
	 * Creates the item option event.
	 * 
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	public ItemOptionEvent(int option, int interfaceId, int id, int slot) {
		super(option, interfaceId, id, slot);
	}

}