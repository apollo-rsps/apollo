package org.apollo.game.event.impl;

/**
 * The first {@link PlayerActionEvent}.
 * 
 * @author Major
 */
public final class FirstPlayerActionEvent extends PlayerActionEvent {

	/**
	 * Creates a first player action event.
	 * 
	 * @param playerIndex The index of the clicked player.
	 */
	public FirstPlayerActionEvent(int playerIndex) {
		super(1, playerIndex);
	}

}