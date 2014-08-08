package org.apollo.game.message.impl;

import org.apollo.game.message.Message;
import org.apollo.game.model.Item;

/**
 * A {@link Message} sent to the client that adds an item to a tile.
 * 
 * @author Major
 */
public final class AddTileItemMessage extends Message {

	/**
	 * The item to add to the tile.
	 */
	private final Item item;

	/**
	 * The position offset
	 */
	private final int positionOffset;

	/**
	 * Creates an add tile item message.
	 * 
	 * @param item The item to add to the tile.
	 */
	public AddTileItemMessage(Item item) {
		this(item, 0);
	}

	/**
	 * Creates an add tile item message.
	 * 
	 * @param item The item to add to the tile.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public AddTileItemMessage(Item item, int positionOffset) {
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