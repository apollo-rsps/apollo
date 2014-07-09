package org.apollo.game.event.impl;

/**
 * The fifth {@link PlayerActionEvent}.
 * 
 * @author Major
 */
public final class FifthPlayerActionEvent extends PlayerActionEvent {

    /**
     * Creates a fifth player action event.
     * 
     * @param playerIndex The index of the clicked player.
     */
    public FifthPlayerActionEvent(int playerIndex) {
	super(5, playerIndex);
    }

}