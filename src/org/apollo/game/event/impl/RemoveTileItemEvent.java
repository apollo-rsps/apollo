package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent to the client to remove an item from a tile.
 * 
 * @author Major
 */
public final class RemoveTileItemEvent extends Event {

	/**
	 * The item.
	 */
	private final int id;

	/**
	 * The offset from the client's base position.
	 */
	private final int positionOffset;

	/**
	 * Creates a remove tile item event.
	 * 
	 * @param id The id of the item to remove.
	 */
	public RemoveTileItemEvent(int id) {
		this(id, 0);
	}

	/**
	 * Creates a remove tile item event.
	 * 
	 * @param id The id of the item to remove.
	 * @param positionOffset The offset from the 'base' position.
	 */
	public RemoveTileItemEvent(int id, int positionOffset) {
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