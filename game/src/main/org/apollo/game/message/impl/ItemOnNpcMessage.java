package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player uses an item on a npc.
 *
 * @author Lmctruck30
 */

public final class ItemOnNpcMessage extends Message {

	/**
	 * Item id used on the Npc.
	 */
	private final int itemId;

	/**
	 * Npc index that the item was used on.
	 */
	private final int npcIndex;

	/**
	 * Item slot from inventory.
	 */
	private final int itemSlot;

	/**
	 * Interface item was used on the npc.
	 */
	private final int interfaceId;

	/**
	 * Creates a new item on npc message.
	 *
	 * @param itemId The item id used on the npc.
	 * @param npcIndex The npc server index.
	 * @param itemSlot The item slot from the players inventory.
	 * @param interfaceId The interface id.
	 */
	public ItemOnNpcMessage(int itemId, int npcIndex, int itemSlot, int interfaceId) {
		this.itemId = itemId;
		this.itemSlot = itemSlot;
		this.npcIndex = npcIndex;
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the item id.
	 *
	 * @return The itemId.
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * Gets the item slot from players inventory.
	 *
	 * @return The itemSlot.
	 */
	public int getItemSlot() {
		return itemSlot;
	}

	/**
	 * Gets the npc index the server set for the npc.
	 *
	 * @return The npcIndex.
	 */
	public int getNpcIndex() {
		return npcIndex;
	}

	/**
	 * Gets the interface id from the client.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}
