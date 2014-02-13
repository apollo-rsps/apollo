package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Item;
import org.apollo.game.model.def.ItemDefinition;

/**
 * An {@link Event} sent to tell the client to display an item on a tile.
 * 
 * @author Major
 */
public final class SetTileItemEvent extends Event {

	/**
	 * The {@link Item}.
	 */
	private final Item item;

	/**
	 * The offset from the client's base position.
	 */
	private final int positionOffset;

	/**
	 * The previous amount of the item (if it is being updated).
	 */
	private final int previousAmount;

	/**
	 * Indicates whether the item is stackable.
	 */
	private final boolean stackable;

	/**
	 * Indicates whether an existing item is being updated.
	 */
	private final boolean updating;

	/**
	 * Creates a new event that will remove the item with the specified id from the tile.
	 * 
	 * @param id The id of the item.
	 */
	public SetTileItemEvent(int id) {
		this(id, 0, ItemDefinition.lookup(id).isStackable(), false, 0, 0);
	}

	/**
	 * Creates a new event that will add an item with the specified id and amount to the tile.
	 * 
	 * @param id The id of the item.
	 * @param amount The amount of the item.
	 */
	public SetTileItemEvent(int id, int amount) {
		this(id, amount, ItemDefinition.lookup(id).isStackable(), false, 0, 0);
	}

	/**
	 * Creates a new event that updates the previous amount of the item.
	 * 
	 * @param id The id of the item.
	 * @param amount The amount of the item.
	 * @param previousAmount The previous amount of the item.
	 */
	public SetTileItemEvent(int id, int amount, int previousAmount) {
		this(id, amount, true, true, previousAmount, 0);
	}

	/**
	 * Creates a new set tile item event.
	 * 
	 * @param id The id of the item.
	 * @param amount The new amount of the item.
	 * @param stackable Whether the item is stackable or not.
	 * @param positionOffset The offset from the client's base position.
	 * @param updating If the item is being updated or not.
	 * @param previousAmount The previous amount of the item.
	 */
	public SetTileItemEvent(int id, int amount, boolean stackable, boolean updating, int previousAmount,
			int positionOffset) {
		if (id < 0 || amount < 0 || previousAmount < 0) {
			throw new IllegalArgumentException("The id, amount, and previous amount must be 0 or greater.");
		}
		this.item = new Item(id, amount);
		this.stackable = stackable;
		this.updating = updating;
		this.previousAmount = previousAmount;
		this.positionOffset = positionOffset;
	}

	/**
	 * Gets the amount of the item.
	 * 
	 * @return The amount.
	 */
	public int getAmount() {
		return item.getAmount();
	}

	/**
	 * Gets the id of the item.
	 * 
	 * @return The item.
	 */
	public int getId() {
		return item.getId();
	}

	/**
	 * Gets the offset from the client's base position.
	 * 
	 * @return The offset.
	 */
	public int getPositionOffset() {
		return positionOffset;
	}

	/**
	 * Gets the previous amount of the item.
	 * 
	 * @return The previous amount.
	 */
	public int getPreviousAmount() {
		return previousAmount;
	}

	/**
	 * Indicates whether this item is stackable or not.
	 * 
	 * @return {@code true} if the item is stackable, otherwise {@code false}.
	 */
	public boolean isStackable() {
		return stackable;
	}

	/**
	 * Indicates whether this item is being updated or not.
	 * 
	 * @return {@code true} if the item is being updated, otherwise {@code false}.
	 */
	public boolean isUpdating() {
		return updating;
	}

}