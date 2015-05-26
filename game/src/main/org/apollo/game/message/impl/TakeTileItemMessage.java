package org.apollo.game.message.impl;

import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent by the client to pick up an item on a tile.
 *
 * @author Major
 */
public final class TakeTileItemMessage extends Message {

	/**
	 * The id of the item.
	 */
	private final int id;

	/**
	 * The position of the tile.
	 */
	private final Position position;

	/**
	 * Creates a new take tile item message.
	 *
	 * @param id The id of the item.
	 * @param position The position of the tile.
	 */
	public TakeTileItemMessage(int id, Position position) {
		this.id = id;
		this.position = position;
	}

	/**
	 * Gets the id of the item.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the position of the tile.
	 *
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

}