package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

public class IfMoveSubMessage extends Message {

	private final int fromComponent;
	private final int toComponent;

	public IfMoveSubMessage(int fromComponent, int toComponent) {
		this.fromComponent = fromComponent;
		this.toComponent = toComponent;
	}

	public int getFromComponent() {
		return fromComponent;
	}

	public int getToComponent() {
		return toComponent;
	}
}
