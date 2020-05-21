package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.SetMapFlagMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class SetMapFlagEncoder extends MessageEncoder<SetMapFlagMessage> {

	@Override
	public GamePacket encode(SetMapFlagMessage message) {
		final var builder = new GamePacketBuilder(67, PacketType.FIXED);
		builder.put(DataType.BYTE, message.getY());
		builder.put(DataType.BYTE, message.getX());
		return builder.toGamePacket();
	}
}
