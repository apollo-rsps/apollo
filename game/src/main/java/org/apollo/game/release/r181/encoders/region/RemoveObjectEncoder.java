package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.RemoveObjectMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class RemoveObjectEncoder extends MessageEncoder<RemoveObjectMessage> {

	@Override
	public GamePacket encode(RemoveObjectMessage message) {
		final var builder = new GamePacketBuilder(13, PacketType.FIXED);
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getType() << 2 | message.getOrientation());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getPositionOffset());
		return builder.toGamePacket();
	}
}
