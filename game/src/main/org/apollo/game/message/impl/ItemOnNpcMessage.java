package org.apollo.game.message.impl;

import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client when a player uses an item on a npc.
 *
 * @author Lmctruck30
 */
public final class ItemOnNpcMessage extends Message {

	/**
	 * The id of the item used on the Npc.
	 */
	private final int id;

	/**
	 * The index of the Npc the item was used on.
	 */
	private final int index;

	/**
	 * Item slot from inventory.
	 */
	private final int slot;

	/**
	 * The id of the interface the item was in, before it was used.
	 */
	private final int interfaceId;

	/**
	 * Creates the ItemOnNpcMessage.
	 *
	 * @param id The id of the item used on the Npc.
	 * @param index The index of the Npc the item was used on.
	 * @param slot The slot the item was in, in the players inventory.
	 * @param interfaceId The id of the interface the item was in, before it was used.
	 */
	public ItemOnNpcMessage(int id, int index, int slot, int interfaceId) {
		this.id = id;
		this.slot = slot;
		this.index = index;
		this.interfaceId = interfaceId;
	}

	/**
	 * Gets the item id.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the slot the item was in.
	 *
	 * @return The slot.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Gets the index of the Npc the item was used on.
	 *
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the id of the interface the item was in, before it was used.
	 *
	 * @return The interface id.
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

}
