package org.apollo.game.release.r377;

import org.apollo.game.message.impl.ServerChatMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link ServerChatMessage}.
 *
 * @author Graham
 */
public final class ServerMessageMessageEncoder extends MessageEncoder<ServerChatMessage> {

	@Override
	public GamePacket encode(ServerChatMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(63, PacketType.VARIABLE_BYTE);
		builder.putString(message.getMessage());
		return builder.toGamePacket();
	}

}