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
	 *
	 */
	public ItemOnNpcMessage(int itemId, int npcIndex, int itemSlot, int interfaceId) {
		this.itemId = itemId;
		this.itemSlot = itemSlot;
		this.npcIndex = npcIndex;
		this.interfaceId = interfaceId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getItemSlot() {
		return itemSlot;
	}

	public int getNpcIndex() {
		return npcIndex;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

}
