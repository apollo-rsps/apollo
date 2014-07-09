package org.apollo.game.event.impl;

/**
 * The second {@link PlayerActionEvent}.
 * 
 * @author Major
 */
public final class SecondPlayerActionEvent extends PlayerActionEvent {

    /**
     * Creates a second player action event.
     * 
     * @param playerIndex The index of the clicked player.
     */
    public SecondPlayerActionEvent(int playerIndex) {
	super(2, playerIndex);
    }

}