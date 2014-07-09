package org.apollo.game.event.impl;

/**
 * The fourth {@link PlayerActionEvent}.
 * 
 * @author Major
 */
public final class FourthPlayerActionEvent extends PlayerActionEvent {

    /**
     * Creates a fourth player action event.
     * 
     * @param playerIndex The index of the clicked player.
     */
    public FourthPlayerActionEvent(int playerIndex) {
	super(4, playerIndex);
    }

}