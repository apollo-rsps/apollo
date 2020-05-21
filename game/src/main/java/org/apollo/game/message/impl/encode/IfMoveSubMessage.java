package org.apollo.game.message.impl.encode;

import org.apollo.net.message.Message;

public class IfMoveSubMessage extends Message {

	private final int fromPackedInterface;
	private final int toPackedInterface;

	public IfMoveSubMessage(int fromPackedInterface, int toPackedInterface) {
		this.fromPackedInterface = fromPackedInterface;
		this.toPackedInterface = toPackedInterface;
	}

	public int getFromPackedInterface() {
		return fromPackedInterface;
	}

	public int getToPackedInterface() {
		return toPackedInterface;
	}
}
