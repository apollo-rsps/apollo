package org.apollo.game.message.impl;

/**
 * The first {@link PlayerActionMessage}.
 * 
 * @author Major
 */
public final class FirstPlayerActionMessage extends PlayerActionMessage {

	/**
	 * Creates a first player action message.
	 * 
	 * @param playerIndex The index of the clicked player.
	 */
	public FirstPlayerActionMessage(int playerIndex) {
		super(1, playerIndex);
	}

}