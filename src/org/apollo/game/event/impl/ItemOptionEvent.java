package org.apollo.game.event.impl;

/**
 * An {@link InventoryItemEvent} sent by the client when an item's option is clicked (e.g. equip, eat, drink, etc). Note
 * that the actual event sent by the client is one of the five item option events, but this is the event that should be
 * intercepted (and the option verified).
 * 
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