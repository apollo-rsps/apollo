package org.apollo.game.message.impl;

import org.apollo.game.model.Position;

/**
 * The first {@link ObjectActionMessage}.
 * 
 * @author Graham
 */
public final class FirstObjectActionMessage extends ObjectActionMessage {

	/**
	 * Creates the first object action message.
	 * 
	 * @param id The id.
	 * @param position The position.
	 */
	public FirstObjectActionMessage(int id, Position position) {
		super(1, id, position);
	}

}