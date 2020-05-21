package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetModelRotateMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetModelRotateEncoder extends MessageEncoder<IfSetModelRotateMessage> {
	@Override
	public GamePacket encode(IfSetModelRotateMessage message) {
		final var builder = new GamePacketBuilder(41, PacketType.FIXED);

		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getRotationY());
		builder.put(DataType.SHORT, DataOrder.LITTLE, message.getRotationX());
		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getPackedInterface());

		return builder.toGamePacket();
	}
}
