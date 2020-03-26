package org.apollo.game.message.impl.decode;

import org.apollo.game.message.impl.decode.InventoryItemMessage;

import java.util.OptionalInt;

/**
 * A {@link InventoryItemMessage} sent by the client when a player casts a spell on an inventory item.
 *
 * @author Khaled Abdeljaber
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
	public MagicOnItemMessage(int interfaceId, int componentId, int id, int slot, int spell) {
		super(OptionalInt.empty(), interfaceId, componentId, id, slot);
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