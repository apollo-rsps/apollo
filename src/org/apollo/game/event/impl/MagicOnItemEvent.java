package org.apollo.game.event.impl;

/**
 * An event sent by the client when a player casts a spell on an inventory item.
 * 
 * @author Chris Fletcher
 */
public final class MagicOnItemEvent extends InventoryItemEvent {

	/**
	 * The spell id.
	 */
	private final int spell;

	/**
	 * Creates a new magic on item event.
	 * 
	 * @param interfaceId The interface id.
	 * @param id The item id.
	 * @param slot The item slot.
	 * @param spell The spell id.
	 */
	public MagicOnItemEvent(int interfaceId, int id, int slot, int spell) {
		super(0, interfaceId, id, slot);
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