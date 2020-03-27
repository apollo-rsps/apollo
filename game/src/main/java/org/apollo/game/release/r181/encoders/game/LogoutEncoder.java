package org.apollo.game.release.r181.encoders.game;

import org.apollo.game.message.impl.encode.LogoutMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class LogoutEncoder extends MessageEncoder<LogoutMessage> {
	@Override
	public GamePacket encode(LogoutMessage message) {
		final var builder = new GamePacketBuilder(1, PacketType.FIXED);
		return builder.toGamePacket();
	}
}
