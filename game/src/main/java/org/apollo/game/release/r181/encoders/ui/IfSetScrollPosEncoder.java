package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetScrollPosMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetScrollPosEncoder extends MessageEncoder<IfSetScrollPosMessage> {
	@Override
	public GamePacket encode(IfSetScrollPosMessage message) {
		final var builder = new GamePacketBuilder(76, PacketType.FIXED);

		builder.put(DataType.INT, DataOrder.MIDDLE, message.getPackedInterface());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getScrollPosition());

		return builder.toGamePacket();
	}
}
