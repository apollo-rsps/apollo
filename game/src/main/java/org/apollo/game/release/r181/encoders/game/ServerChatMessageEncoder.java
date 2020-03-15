package org.apollo.game.release.r181.encoders.game;

import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

public class ServerChatMessageEncoder extends MessageEncoder<ServerChatMessage> {
	@Override
	public GamePacket encode(ServerChatMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(3, PacketType.VARIABLE_BYTE);
		final var ext = message.getExtension();

		builder.putSmart(message.getType().getId());
		if (ext.isEmpty()) {
			builder.put(DataType.BYTE, 1);
			builder.putString(ext);
		} else {
			builder.put(DataType.BYTE, 0);
		}
		builder.putString(message.getMessage());
		return builder.toGamePacket();
	}
}
