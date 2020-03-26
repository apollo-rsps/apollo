package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetNpcHeadMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetNpcHeadEncoder extends MessageEncoder<IfSetNpcHeadMessage> {
	@Override
	public GamePacket encode(IfSetNpcHeadMessage message) {
		final var builder = new GamePacketBuilder(35, PacketType.FIXED);

		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, message.getNpcId());
		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getPackedInterface());

		return builder.toGamePacket();
	}
}
