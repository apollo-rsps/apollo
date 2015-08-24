package org.apollo.game.message.impl;

import java.util.OptionalInt;

/**
 * A {@link InventoryItemMessage} sent by the client when a player casts a spell on an inventory item.
 *
 * @author Chris Fletcher
 */
public final class MagicOnItemMessage extends InventoryItemMessage {

	/**
	 * The spell id.
	 */
	private final int spell;

	/**
	 * Creates a new magic on item message.
	 *
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 * @param spell The spell id.
	 */
	public MagicOnItemMessage(int interfaceId, int id, int slot, int spell) {
		super(OptionalInt.empty(), interfaceId, id, slot);
		this.spell = spell;
	}

	/**
	 * Gets the spell id.
	 *
	 * @return The spell id.
	 */
	public int getSpellId() {
		return spell;
	}

}