package org.apollo.game.message.impl;

/**
 * The fourth {@link PlayerActionMessage}.
 * 
 * @author Major
 */
public final class FourthPlayerActionMessage extends PlayerActionMessage {

	/**
	 * Creates a fourth player action message.
	 * 
	 * @param playerIndex The index of the clicked player.
	 */
	public FourthPlayerActionMessage(int playerIndex) {
		super(4, playerIndex);
	}

}