package org.apollo.game.message.impl;

import org.apollo.game.model.Item;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client that adds an item to a tile.
 *
 * @author Major
 */
public final class SendTileItemMessage extends RegionUpdateMessage {

	/**
	 * The item to add to the tile.
	 */
	private final Item item;

	/**
	 * The position offset
	 */
	private final int positionOffset;

	/**
	 * Creates the SendTileItemMessage.
	 *
	 * @param item The item to add to the tile.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public SendTileItemMessage(Item item, int positionOffset) {
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

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SendTileItemMessage) {
			SendTileItemMessage other = (SendTileItemMessage) obj;
			return item.equals(other.item) && positionOffset == other.positionOffset;
		}

		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return item.hashCode() * prime + positionOffset;
	}

	@Override
	public int priority() {
		return LOW_PRIORITY;
	}

}