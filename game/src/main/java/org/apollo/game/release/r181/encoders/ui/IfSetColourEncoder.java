package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetColourMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetColourEncoder extends MessageEncoder<IfSetColourMessage> {
	@Override
	public GamePacket encode(IfSetColourMessage message) {
		final var builder = new GamePacketBuilder(24, PacketType.FIXED);

		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getPackedInterface());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getPackedColours());
		return builder.toGamePacket();
	}
}
