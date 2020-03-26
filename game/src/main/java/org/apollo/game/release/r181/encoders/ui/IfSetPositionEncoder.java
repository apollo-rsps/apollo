package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetPositionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetPositionEncoder extends MessageEncoder<IfSetPositionMessage> {
	@Override
	public GamePacket encode(IfSetPositionMessage message) {
		final var builder = new GamePacketBuilder(59, PacketType.FIXED);

		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getPackedInterface());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getY());
		builder.put(DataType.SHORT, message.getX());

		return builder.toGamePacket();
	}
}
