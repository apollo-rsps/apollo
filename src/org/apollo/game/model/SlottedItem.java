package org.apollo.game.model;

/**
 * A class which contains an {@link Item} and its corresponding slot.
 * @author Graham
 */
public final class SlottedItem {

	/**
	 * The slot.
	 */
	private final int slot;

	/**
	 * The item.
	 */
	private final Item item;

	/**
	 * Creates a new slotted item.
	 * @param slot The slot.
	 * @param item The item.
	 */
	public SlottedItem(int slot, Item item) {
		this.slot = slot;
		this.item = item;
	}

	/**
	 * Gets the slot.
	 * @return The slot.
	 */
	public int getSlot() {
		return slot;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

}
