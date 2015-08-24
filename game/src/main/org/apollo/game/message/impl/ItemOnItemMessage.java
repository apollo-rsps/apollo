package org.apollo.game.message.impl;

import java.util.OptionalInt;

/**
 * A {@link InventoryItemMessage} sent by the client when a player uses one inventory item on another.
 *
 * @author Chris Fletcher
 */
public final class ItemOnItemMessage extends InventoryItemMessage {

	/**
	 * The id of the target item.
	 */
	private final int targetId;

	/**
	 * The interface id of the target item.
	 */
	private final int targetInterface;

	/**
	 * The slot of the target item.
	 */
	private final int targetSlot;

	/**
	 * Creates a new item-on-item message.
	 *
	 * @param usedInterface The interface id of the used item.
	 * @param usedId The id of the used item.
	 * @param usedSlot The slot of the target item.
	 * @param targetInterface The interface id of the target item.
	 * @param targetId The id of the target item.
	 * @param targetSlot The slot of the target item.
	 */
	public ItemOnItemMessage(int usedInterface, int usedId, int usedSlot, int targetInterface, int targetId, int targetSlot) {
		super(OptionalInt.empty(), usedInterface, usedId, usedSlot);
		this.targetInterface = targetInterface;
		this.targetSlot = targetSlot;
		this.targetId = targetId;
	}

	/**
	 * Gets the id of the target item.
	 *
	 * @return The target item's interface id.
	 */
	public int getTargetId() {
		return targetId;
	}

	/**
	 * Gets the interface id of the target item.
	 *
	 * @return The target item's interface id.
	 */
	public int getTargetInterfaceId() {
		return targetInterface;
	}

	/**
	 * Gets the slot of the target item.
	 *
	 * @return The slot of the target item.
	 */
	public int getTargetSlot() {
		return targetSlot;
	}

}