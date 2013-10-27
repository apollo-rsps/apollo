package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An event sent by the client when two items are switched.
 * @author Graham
 */
public final class SwitchItemEvent extends Event {

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * A flag indicating if insertion mode is enabled.
	 */
	private final boolean inserting;

	/**
	 * The old slot.
	 */
	private final int oldSlot;

	/**
	 * The new slot.
	 */
	private final int newSlot;

	/**
	 * Creates a new switch item event.
	 * @param interfaceId The interface id.
	 * @param inserting A flag indicating if the interface is in 'insert' mode
	 * instead of swap mode.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 */
	public SwitchItemEvent(int interfaceId, boolean inserting, int oldSlot, int newSlot) {
		this.interfaceId = interfaceId;
		this.inserting = inserting;
		this.oldSlot = oldSlot;
		this.newSlot = newSlot;
	}

	/**
	 * Gets the interface id.
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Checks if this event is in insertion mode.
	 * @return The insertion flag.
	 */
	public boolean isInserting() {
		return inserting;
	}

	/**
	 * Checks if this event is in swap mode.
	 * @return The swap flag.
	 */
	public boolean isSwapping() {
		return !inserting;
	}

	/**
	 * Gets the old slot.
	 * @return The old slot.
	 */
	public int getOldSlot() {
		return oldSlot;
	}

	/**
	 * Gets the new slot.
	 * @return The new slot.
	 */
	public int getNewSlot() {
		return newSlot;
	}

}
