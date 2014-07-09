package org.apollo.game.event.impl;

import org.apollo.game.model.Position;

/**
 * The first {@link ObjectActionEvent}.
 * 
 * @author Graham
 */
public final class FirstObjectActionEvent extends ObjectActionEvent {

    /**
     * Creates the first object action event.
     * 
     * @param id The id.
     * @param position The position.
     */
    public FirstObjectActionEvent(int id, Position position) {
	super(1, id, position);
    }

}