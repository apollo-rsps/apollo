package org.apollo.game.model;

import org.apollo.game.model.def.ItemDefinition;

/**
 * Represents a single item.
 * @author Graham
 */
public final class Item {

	/**
	 * The item's id.
	 */
	private final int id;

	/**
	 * The amount of items in the stack.
	 */
	private final int amount;

	/**
	 * Creates an item with an amount of {@code 1}.
	 * @param id The item's id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * Creates an item with the specified the amount.
	 * @param id The item's id.
	 * @param amount The amount.
	 * @throws IllegalArgumentException if the amount is negative.
	 */
	public Item(int id, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("negative amount");
		}
		this.id = id;
		this.amount = amount;
	}

	/**
	 * Gets the id.
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the amount.
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the {@link ItemDefinition} which describes this item.
	 * @return The definition.
	 */
	public ItemDefinition getDefinition() {
		return ItemDefinition.forId(id);
	}

	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", amount=" + amount + "]";
	}

}
