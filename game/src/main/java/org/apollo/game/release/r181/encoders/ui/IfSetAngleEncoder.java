package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetAngleMessage;
import org.apollo.game.message.impl.encode.IfSetNpcHeadMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetAngleEncoder extends MessageEncoder<IfSetAngleMessage> {
	@Override
	public GamePacket encode(IfSetAngleMessage message) {
		final var builder = new GamePacketBuilder(50, PacketType.FIXED);

		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getRotationX());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getZoom());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getRotationY());
		builder.put(DataType.INT, DataOrder.MIDDLE, message.getPackedInterface());

		return builder.toGamePacket();
	}
}
