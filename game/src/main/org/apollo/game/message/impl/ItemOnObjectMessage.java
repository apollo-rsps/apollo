package org.apollo.game.message.impl;

import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

import java.util.OptionalInt;

/**
 * A {@link Message} sent by the client when an item is used on an object.
 *
 * @author Major
 */
public final class ItemOnObjectMessage extends InventoryItemMessage {

	/**
	 * The object id the item was used on.
	 */
	private final int objectId;

	/**
	 * The position of the object.
	 */
	private final Position position;

	/**
	 * Creates an item on object message.
	 *
	 * @param interfaceId The interface id.
	 * @param itemId The item id.
	 * @param itemSlot The slot the item is in.
	 * @param objectId The object id.
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 */
	public ItemOnObjectMessage(int interfaceId, int itemId, int itemSlot, int objectId, int x, int y) {
		super(OptionalInt.empty(), interfaceId, itemId, itemSlot);
		this.objectId = objectId;
		position = new Position(x, y);
	}

	/**
	 * Gets the object id.
	 *
	 * @return The object id.
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * Gets the position of the object.
	 *
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

}