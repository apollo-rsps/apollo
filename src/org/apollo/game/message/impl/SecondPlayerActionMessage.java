package org.apollo.game.message.impl;

/**
 * The second {@link PlayerActionMessage}.
 * 
 * @author Major
 */
public final class SecondPlayerActionMessage extends PlayerActionMessage {

	/**
	 * Creates a second player action message.
	 * 
	 * @param playerIndex The index of the clicked player.
	 */
	public SecondPlayerActionMessage(int playerIndex) {
		super(2, playerIndex);
	}

}