package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client that represents some sort of action on an item. Note that the actual event sent
 * by the client is one of the five item action events, but this is the event that should be intercepted (and the option
 * verified).
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