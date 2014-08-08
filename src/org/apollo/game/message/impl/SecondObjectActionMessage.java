package org.apollo.game.message.impl;

import org.apollo.game.model.Position;

/**
 * The second {@link ObjectActionMessage}.
 * 
 * @author Graham
 */
public final class SecondObjectActionMessage extends ObjectActionMessage {

	/**
	 * Creates the second object action message.
	 * 
	 * @param id The id.
	 * @param position The position.
	 */
	public SecondObjectActionMessage(int id, Position position) {
		super(2, id, position);
	}

}