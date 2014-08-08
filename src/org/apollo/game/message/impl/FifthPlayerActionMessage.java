package org.apollo.game.message.impl;

/**
 * The fifth {@link PlayerActionMessage}.
 * 
 * @author Major
 */
public final class FifthPlayerActionMessage extends PlayerActionMessage {

	/**
	 * Creates a fifth player action message.
	 * 
	 * @param playerIndex The index of the clicked player.
	 */
	public FifthPlayerActionMessage(int playerIndex) {
		super(5, playerIndex);
	}

}