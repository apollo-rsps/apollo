package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfOpenSubMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

public class IfOpenSubMessageEncoder extends MessageEncoder<IfOpenSubMessage> {
	@Override
	public GamePacket encode(IfOpenSubMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(77, PacketType.FIXED);
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getType().ordinal());
		builder.put(DataType.INT, DataOrder.MIDDLE, message.getParentComponent());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getId());
		return builder.toGamePacket();
	}
}
