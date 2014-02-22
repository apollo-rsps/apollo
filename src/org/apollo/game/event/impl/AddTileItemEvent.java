package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Item;

/**
 * An {@link Event} sent to the client that adds an item to a tile.
 * 
 * @author Major
 */
public final class AddTileItemEvent extends Event {

	/**
	 * The item to add to the tile.
	 */
	private final Item item;

	/**
	 * The position offset
	 */
	private final int positionOffset;

	/**
	 * Creates an add tile item event.
	 * 
	 * @param item The item to add to the tile.
	 */
	public AddTileItemEvent(Item item) {
		this(item, 0);
	}

	/**
	 * Creates an add tile item event.
	 * 
	 * @param item The item to add to the tile.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public AddTileItemEvent(Item item, int positionOffset) {
		this.item = item;
		this.positionOffset = positionOffset;
	}

	/**
	 * Gets the id of the item.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return item.getId();
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
	 * Gets the offset from the 'base' position.
	 * 
	 * @return The offset.
	 */
	public int getPositionOffset() {
		return positionOffset;
	}

}