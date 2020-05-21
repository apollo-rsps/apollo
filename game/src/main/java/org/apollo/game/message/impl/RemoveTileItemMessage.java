package org.apollo.game.message.impl;

import org.apollo.game.model.Item;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to remove an item from a tile.
 *
 * @author Major
 */
public final class RemoveTileItemMessage extends RegionUpdateMessage {

	/**
	 * The id of the Item to remove.
	 */
	private final int id;

	/**
	 * The offset from the client's base position.
	 */
	private final int positionOffset;

	/**
	 * Creates the RemoveTileItemMessage.
	 *
	 * @param id The id of the {@link Item} to remove.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public RemoveTileItemMessage(int id, int positionOffset) {
		this.id = id;
		this.positionOffset = positionOffset;
	}

	/**
	 * Creates the RemoveTileItemMessage.
	 *
	 * @param item The {@link Item} to remove.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public RemoveTileItemMessage(Item item, int positionOffset) {
		this(item.getId(), positionOffset);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RemoveTileItemMessage) {
			RemoveTileItemMessage other = (RemoveTileItemMessage) obj;
			return id == other.id && positionOffset == other.positionOffset;
		}

		return false;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * id + positionOffset;
	}

	@Override
	public int priority() {
		return HIGH_PRIORITY;
	}

}