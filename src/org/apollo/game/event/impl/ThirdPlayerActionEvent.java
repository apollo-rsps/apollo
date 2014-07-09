package org.apollo.game.event.impl;

/**
 * The third {@link PlayerActionEvent}.
 * 
 * @author Major
 */
public final class ThirdPlayerActionEvent extends PlayerActionEvent {

    /**
     * Creates a third player action event.
     * 
     * @param playerIndex The index of the clicked player.
     */
    public ThirdPlayerActionEvent(int playerIndex) {
	super(3, playerIndex);
    }

}