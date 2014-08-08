package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent to the client to remove an item from a tile.
 * 
 * @author Major
 */
public final class RemoveTileItemMessage extends Message {

	/**
	 * The item.
	 */
	private final int id;

	/**
	 * The offset from the client's base position.
	 */
	private final int positionOffset;

	/**
	 * Creates a remove tile item message.
	 * 
	 * @param id The id of the item to remove.
	 */
	public RemoveTileItemMessage(int id) {
		this(id, 0);
	}

	/**
	 * Creates a remove tile item message.
	 * 
	 * @param id The id of the item to remove.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public RemoveTileItemMessage(int id, int positionOffset) {
		this.id = id;
		this.positionOffset = positionOffset;
	}

	/**
	 * Gets the id of the item to remove.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
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