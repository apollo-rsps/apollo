package org.apollo.game.model;

/**
 * A class which contains an {@link Item} and its corresponding slot.
 * 
 * @author Graham
 */
public final class SlottedItem {

	/**
	 * The item.
	 */
	private final Item item;

	/**
	 * The slot.
	 */
	private final int slot;

	/**
	 * Creates a new slotted item.
	 * 
	 * @param slot The slot.
	 * @param item The item.
	 */
	public SlottedItem(int slot, Item item) {
		this.slot = slot;
		this.item = item;
	}

	/**
	 * Gets the item.
	 * 
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	/**
	 * Gets the slot.
	 * 
	 * @return The slot.
	 */
	public int getSlot() {
		return slot;
	}

}