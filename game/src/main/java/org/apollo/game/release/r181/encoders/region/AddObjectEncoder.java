package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.AddObjectMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class AddObjectEncoder extends MessageEncoder<AddObjectMessage> {
	@Override
	public GamePacket encode(AddObjectMessage message) {
		final var builder = new GamePacketBuilder(6, PacketType.FIXED);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, message.getType() << 2 | message.getOrientation());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getId());
		return builder.toGamePacket();
	}
}
