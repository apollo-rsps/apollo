package org.apollo.game.model;

import org.apollo.game.model.def.ItemDefinition;

/**
 * Represents a single item.
 * 
 * @author Graham
 */
public final class Item {

	/**
	 * The amount of items in the stack.
	 */
	private final int amount;

	/**
	 * The item's id.
	 */
	private final int id;

	/**
	 * Creates an item with an amount of {@code 1}.
	 * 
	 * @param id The item's id.
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * Creates an item with the specified the amount.
	 * 
	 * @param id The item's id.
	 * @param amount The amount.
	 * @throws IllegalArgumentException If the amount is negative.
	 */
	public Item(int id, int amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("negative amount");
		}
		this.id = id;
		this.amount = amount;
	}

	/**
	 * Gets the amount.
	 * 
	 * @return The amount.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Gets the {@link ItemDefinition} which describes this item.
	 * 
	 * @return The definition.
	 */
	public ItemDefinition getDefinition() {
		return ItemDefinition.lookup(id);
	}

	/**
	 * Gets the id.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	@Override
	public String toString() {
		return Item.class.getName() + " [id=" + id + ", amount=" + amount + "]";
	}

}