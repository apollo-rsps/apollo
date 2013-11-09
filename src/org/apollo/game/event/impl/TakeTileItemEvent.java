package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An {@link Event} sent by the client indicating a request to pick up an item on a tile.
 * 
 * @author Major
 */
public final class TakeTileItemEvent extends Event {

	/**
	 * The id of the item.
	 */
	private final int id;

	/**
	 * The position of the tile.
	 */
	private final Position position;

	/**
	 * Creates a new take tile item event.
	 * 
	 * @param id The id of the item.
	 * @param position The position of the tile.
	 */
	public TakeTileItemEvent(int id, Position position) {
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