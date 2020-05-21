package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.MobResetAnimsMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class MobResetAnimsEncoder extends MessageEncoder<MobResetAnimsMessage> {

	@Override
	public GamePacket encode(MobResetAnimsMessage message) {
		final var builder = new GamePacketBuilder(28, PacketType.FIXED);
		return builder.toGamePacket();
	}
}
