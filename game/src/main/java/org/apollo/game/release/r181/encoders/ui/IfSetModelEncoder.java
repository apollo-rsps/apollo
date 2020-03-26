package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetModelMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetModelEncoder extends MessageEncoder<IfSetModelMessage> {
	@Override
	public GamePacket encode(IfSetModelMessage message) {
		final var builder = new GamePacketBuilder(31, PacketType.FIXED);

		builder.put(DataType.INT, DataOrder.MIDDLE, message.getPackedInterface());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getModel());

		return builder.toGamePacket();
	}
}
