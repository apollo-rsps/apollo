package org.apollo.game.message.impl;

import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.message.Message;

public final class PlayerSynchronizationMessage extends Message {


	private final GamePacketBuilder[] nsnBuilders;
	private final GamePacketBuilder blockBuilder;

	public PlayerSynchronizationMessage(GamePacketBuilder[] nsnBuilders, GamePacketBuilder blockBuilder) {
		this.nsnBuilders = nsnBuilders;
		this.blockBuilder = blockBuilder;
	}

	public GamePacketBuilder[] getNsnBuilders() {
		return nsnBuilders;
	}

	public GamePacketBuilder getBlockBuilder() {
		return blockBuilder;
	}
}