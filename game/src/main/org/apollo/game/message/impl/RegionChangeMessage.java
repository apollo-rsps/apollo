package org.apollo.game.message.impl;

import org.apollo.game.model.Position;
import org.apollo.net.message.Message;

/**
 * A {@link Message} sent to the client instructing it to load the specified region.
 *
 * @author Graham
 */
public final class RegionChangeMessage extends Message {

	/**
	 * The position of the region to load.
	 */
	private final Position position;

	/**
	 * Creates the region changed message.
	 *
	 * @param position The position of the region.
	 */
	public RegionChangeMessage(Position position) {
		this.position = position;
	}

	/**
	 * Gets the position of the region to load.
	 *
	 * @return The position of the region to load.
	 */
	public Position getPosition() {
		return position;
	}

}