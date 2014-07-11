package org.apollo.game.event.impl;

import org.apollo.game.event.Event;

/**
 * An {@link Event} sent by the client representing the clicking of a player menu action. Note that the actual event
 * sent by the client is one of the five player action events, but this is the event that should be intercepted (and the
 * option verified).
 * 
 * @author Major
 */
public abstract class PlayerActionEvent extends Event {

	/**
	 * The option number.
	 */
	private final int option;

	/**
	 * The index of the clicked player.
	 */
	private final int index;

	/**
	 * Creates a player action event.
	 * 
	 * @param option The option number.
	 * @param playerIndex The index of the player.
	 */
	public PlayerActionEvent(int option, int playerIndex) {
		this.option = option;
		this.index = playerIndex;
	}

	/**
	 * Gets the menu action number (i.e. the action event 'option') clicked.
	 * 
	 * @return The option number.
	 */
	public int getOption() {
		return option;
	}

	/**
	 * Gets the index of the clicked player.
	 * 
	 * @return The index.
	 */
	public int getIndex() {
		return index;
	}

}