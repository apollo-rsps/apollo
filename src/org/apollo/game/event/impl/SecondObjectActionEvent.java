package org.apollo.game.event.impl;

import org.apollo.game.model.Position;

/**
 * An event sent when the second option at an object is used.
 * @author Graham
 */
public final class SecondObjectActionEvent extends ObjectActionEvent {

	/**
	 * Creates the second object action event.
	 * @param id The id.
	 * @param position The position.
	 */
	public SecondObjectActionEvent(int id, Position position) {
		super(2, id, position);
	}

}
