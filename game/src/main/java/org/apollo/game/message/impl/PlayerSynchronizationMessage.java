package org.apollo.game.message.impl;

import org.apollo.game.model.entity.PlayerUpdateInfo;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.message.Message;

public final class PlayerSynchronizationMessage extends Message {


	private final GamePacketBuilder info;

	public PlayerSynchronizationMessage(GamePacketBuilder info) {
		this.info = info;
	}

	public GamePacketBuilder getInfo() {
		return info;
	}
}