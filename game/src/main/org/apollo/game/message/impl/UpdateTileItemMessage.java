package org.apollo.game.message.impl;

import org.apollo.game.model.Item;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client to update the amount of an item display on a tile.
 *
 * @author Major
 */
public final class UpdateTileItemMessage extends RegionUpdateMessage {

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
	 * Creates a new message that updates the previous amount of the item.
	 *
	 * @param item The item to be placed.
	 * @param previousAmount The previous amount of the item.
	 */
	public UpdateTileItemMessage(Item item, int previousAmount) {
		this(item, previousAmount, 0);
	}

	/**
	 * Creates a new set tile item message.
	 *
	 * @param item The item to be placed.
	 * @param previousAmount The previous amount of the item.
	 * @param positionOffset The offset from the client's base position.
	 */
	public UpdateTileItemMessage(Item item, int previousAmount, int positionOffset) {
		this.item = item;
		this.previousAmount = previousAmount;
		this.positionOffset = positionOffset;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof UpdateTileItemMessage) {
			UpdateTileItemMessage other = (UpdateTileItemMessage) obj;
			return item.equals(other.item) && previousAmount == other.previousAmount && positionOffset == other.positionOffset;
		}

		return false;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = item.hashCode() * prime + positionOffset;
		return result * prime + previousAmount;
	}

	@Override
	public int priority() {
		return LOW_PRIORITY;
	}

}