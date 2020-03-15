package org.apollo.game.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apollo.cache.def.ItemDefinition;

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
	 * The item definition.
	 */
	private final ItemDefinition definition;

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
		Preconditions.checkArgument(amount >= 0, "Amount cannot be negative.");
		this.id = id;
		this.amount = amount;
		definition = ItemDefinition.lookup(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Item) {
			Item other = (Item) obj;
			return id == other.id && amount == other.amount;
		}

		return false;
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
	 * Gets the {@link ItemDefinition} that describes this item.
	 *
	 * @return The definition.
	 */
	public ItemDefinition getDefinition() {
		return definition;
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
	public int hashCode() {
		final int prime = 31;
		return amount * prime + id;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("id", id).add("amount", amount).toString();
	}

}