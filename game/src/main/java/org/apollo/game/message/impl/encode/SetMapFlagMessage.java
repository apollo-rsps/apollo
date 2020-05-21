package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

/**
 * The type Set map flag message.
 *
 * @author Khaled Abdeljaber
 */
public class SetMapFlagMessage extends Message {

	private byte x, y;

	/**
	 * Instantiates a new Set map flag message.
	 *
	 * @param x the x.
	 * @param y the y.
	 */
	public SetMapFlagMessage(byte x, byte y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets x.
	 *
	 * @return the x
	 */
	public byte getX() {
		return x;
	}

	/**
	 * Gets y.
	 *
	 * @return the y
	 */
	public byte getY() {
		return y;
	}
}
