package org.apollo.game.model.inv;

import org.apollo.game.model.Item;

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
	 * Gets the amount of the {@link Item}.
	 *
	 * @return The amount.
	 */
	public int getAmount() {
		return item.getAmount();
	}

	/**
	 * Gets the id of the {@link Item}.
	 *
	 * @return The id.
	 */
	public int getId() {
		return item.getId();
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