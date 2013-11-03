package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} which represents some sort of action on an item in an inventory. Note that this is the parent of
 * both the item option and action event classes, and so cannot be used to determine when one of those events is fired.
 * 
 * @author Chris Fletcher
 */
public abstract class InventoryItemEvent extends Event {

	/**
	 * The item id.
	 */
	private final int id;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The option number (1-5).
	 */
	private final int option;

	/**
	 * The item's slot.
	 */
	private final int slot;

	/**
	 * Creates the item action event.
	 * 
	 * @param option The option number.
	 * @param interfaceId The interface id.
	 * @param id The id.
	 * @param slot The slot.
	 */
	protected InventoryItemEvent(int option, int interfaceId, int id, int slot) {
		this.option = option;
		this.interfaceId = interfaceId;
		this.id = id;
		this.slot = slot;
	}

	/**
	 * Gets the item id.
	 * 
	 * @return The item id.
	 */
	public final int getId() {
		return id;
	}

	/**
	 * Gets the interface id.
	 * 
	 * @return The interface id.
	 */
	public final int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Gets the option number.
	 * 
	 * @return The option number.
	 */
	public final int getOption() {
		return option;
	}

	/**
	 * Gets the slot.
	 * 
	 * @return The slot.
	 */
	public final int getSlot() {
		return slot;
	}

}