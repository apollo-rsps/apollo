package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetItemMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetItemEncoder extends MessageEncoder<IfSetItemMessage> {
	@Override
	public GamePacket encode(IfSetItemMessage message) {
		final var builder = new GamePacketBuilder(11, PacketType.FIXED);

		builder.put(DataType.INT, message.getPackedInterface());
		builder.put(DataType.SHORT, message.getItem());
		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getAmount());

		return builder.toGamePacket();
	}
}
