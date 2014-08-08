package org.apollo.game.message.impl;

import org.apollo.game.message.Message;

/**
 * A {@link Message} sent by the client representing the clicking of a player menu action. Note that the actual message
 * sent by the client is one of the five player action messages, but this is the message that should be intercepted (and the
 * option verified).
 * 
 * @author Major
 */
public abstract class PlayerActionMessage extends Message {

	/**
	 * The option number.
	 */
	private final int option;

	/**
	 * The index of the clicked player.
	 */
	private final int index;

	/**
	 * Creates a player action message.
	 * 
	 * @param option The option number.
	 * @param playerIndex The index of the player.
	 */
	public PlayerActionMessage(int option, int playerIndex) {
		this.option = option;
		this.index = playerIndex;
	}

	/**
	 * Gets the menu action number (i.e. the action message 'option') clicked.
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