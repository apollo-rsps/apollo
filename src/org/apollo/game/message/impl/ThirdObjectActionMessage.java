package org.apollo.game.message.impl;

import org.apollo.game.model.Position;

/**
 * The third {@link ObjectActionMessage}.
 * 
 * @author Graham
 */
public final class ThirdObjectActionMessage extends ObjectActionMessage {

	/**
	 * Creates the third object action message.
	 * 
	 * @param id The id.
	 * @param position The position.
	 */
	public ThirdObjectActionMessage(int id, Position position) {
		super(3, id, position);
	}

}