package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfMoveSubMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

public class IfMoveSubMessageEncoder extends MessageEncoder<IfMoveSubMessage> {

	@Override
	public GamePacket encode(IfMoveSubMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(82, PacketType.FIXED);
		builder.put(DataType.INT, message.getToPackedInterface());
		builder.put(DataType.INT, message.getFromPackedInterface());
		return builder.toGamePacket();
	}
}
