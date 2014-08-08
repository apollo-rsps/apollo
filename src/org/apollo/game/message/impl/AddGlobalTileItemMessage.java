package org.apollo.game.message.impl;

import org.apollo.game.message.Message;
import org.apollo.game.model.Item;

public final class AddGlobalTileItemMessage extends Message {

	/**
	 * The item to add to the tile.
	 */
	private final Item item;

	/**
	 * The position offset
	 */
	private final int positionOffset;

	/**
	 * The index of the player who dropped the item.
	 */
	private final int index;

	/**
	 * Creates the add global tile item message.
	 * 
	 * @param item The item to add to the tile.
	 * @param index The index of the player who dropped the item.
	 */
	public AddGlobalTileItemMessage(Item item, int index) {
		this(item, index, 0);
	}

	/**
	 * Creates the add global tile item message.
	 * 
	 * @param item The item to add to the tile.
	 * @param index The index of the player who dropped the item.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public AddGlobalTileItemMessage(Item item, int index, int positionOffset) {
		this.item = item;
		this.index = index;
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
	 * Gets the index of the player who dropped the item.
	 * 
	 * @return The index.
	 */
	public int getIndex() {
		return index;
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