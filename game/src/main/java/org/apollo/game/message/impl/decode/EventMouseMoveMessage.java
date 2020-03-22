package org.apollo.game.message.impl.decode;

import org.apollo.net.message.Message;

/**
 * @author Khaled Abdeljaber
 */
public class EventMouseMoveMessage extends Message {

	private final int x;
	private final int y;
	private final int dt;

	public EventMouseMoveMessage(int x, int y, int dt) {
		this.x = x;
		this.y = y;
		this.dt = dt;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getDt() {
		return dt;
	}
}
