package org.apollo.game.message.impl;

import org.apollo.game.message.Message;
import org.apollo.game.model.Position;

/**
 * A {@link Message} sent to the client instructing it to load the specified sector.
 * 
 * @author Graham
 */
public final class SectorChangeMessage extends Message {

	/**
	 * The position of the sector to load.
	 */
	private final Position position;

	/**
	 * Creates the sector changed message.
	 * 
	 * @param position The position of the sector.
	 */
	public SectorChangeMessage(Position position) {
		this.position = position;
	}

	/**
	 * Gets the position of the sector to load.
	 * 
	 * @return The position of the sector to load.
	 */
	public Position getPosition() {
		return position;
	}

}