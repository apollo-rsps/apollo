package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when two items are switched.
 *
 * @author Graham
 */
public final class SwitchItemMessage extends Message {

	/**
	 * A flag indicating if insertion mode is enabled.
	 */
	private final boolean inserting;

	/**
	 * The interface id.
	 */
	private final int interfaceId;

	/**
	 * The new slot.
	 */
	private final int newSlot;

	/**
	 * The old slot.
	 */
	private final int oldSlot;

	/**
	 * Creates a new switch item message.
	 *
	 * @param interfaceId The interface id.
	 * @param inserting A flag indicating if the interface is in 'insert' mode instead of swap mode.
	 * @param oldSlot The old slot.
	 * @param newSlot The new slot.
	 */
	public SwitchItemMessage(int interfaceId, boolean inserting, int oldSlot, int newSlot) {
		this.interfaceId = interfaceId;
		this.inserting = inserting;
		this.oldSlot = oldSlot;
		this.newSlot = newSlot;
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
	 * Gets the new slot.
	 *
	 * @return The new slot.
	 */
	public int getNewSlot() {
		return newSlot;
	}

	/**
	 * Gets the old slot.
	 *
	 * @return The old slot.
	 */
	public int getOldSlot() {
		return oldSlot;
	}

	/**
	 * Checks if this message is in insertion mode.
	 *
	 * @return The insertion flag.
	 */
	public boolean isInserting() {
		return inserting;
	}

	/**
	 * Checks if this message is in swap mode.
	 *
	 * @return The swap flag.
	 */
	public boolean isSwapping() {
		return !inserting;
	}

}