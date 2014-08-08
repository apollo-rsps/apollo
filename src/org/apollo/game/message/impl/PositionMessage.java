package org.apollo.game.message.impl;

import org.apollo.game.message.Message;
import org.apollo.game.model.Position;

/**
 * A {@link Message} sent to the client to focus on a specific {@link Position} (on which an action should be performed).
 * 
 * @author Chris Fletcher
 */
public final class PositionMessage extends Message {

	/**
	 * The base position.
	 */
	private final Position base;

	/**
	 * The target position.
	 */
	private final Position position;

	/**
	 * Creates a new position message.
	 * 
	 * @param base The base from which the position is being focused on.
	 * @param position The position to focus on.
	 */
	public PositionMessage(Position base, Position position) {
		this.base = base;
		this.position = position;
	}

	/**
	 * Gets the base position.
	 * 
	 * @return The position.
	 */
	public Position getBase() {
		return base;
	}

	/**
	 * Gets the position to focus on.
	 * 
	 * @return The target position.
	 */
	public Position getPosition() {
		return position;
	}

}