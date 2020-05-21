package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfOpenTopMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

public class IfOpenTopMessageEncoder extends MessageEncoder<IfOpenTopMessage> {
	@Override
	public GamePacket encode(IfOpenTopMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(84, PacketType.FIXED);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getId());
		return builder.toGamePacket();
	}
}
