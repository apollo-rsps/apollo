package org.apollo.game.message.impl;

/**
 * The third {@link PlayerActionMessage}.
 * 
 * @author Major
 */
public final class ThirdPlayerActionMessage extends PlayerActionMessage {

	/**
	 * Creates a third player action message.
	 * 
	 * @param playerIndex The index of the clicked player.
	 */
	public ThirdPlayerActionMessage(int playerIndex) {
		super(3, playerIndex);
	}

}