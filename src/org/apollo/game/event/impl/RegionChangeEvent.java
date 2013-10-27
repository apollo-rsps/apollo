package org.apollo.game.event.impl;

import org.apollo.game.event.Event;
import org.apollo.game.model.Position;

/**
 * An event which indicates that the client should load the specified region.
 * @author Graham
 */
public final class RegionChangeEvent extends Event {

	/**
	 * The position of the region to load.
	 */
	private final Position position;

	/**
	 * Creates the region changed event.
	 * @param position The position of the region.
	 */
	public RegionChangeEvent(Position position) {
		this.position = position;
	}

	/**
	 * Gets the position of the region to load.
	 * @return The position of the region to load.
	 */
	public Position getPosition() {
		return position;
	}

}
