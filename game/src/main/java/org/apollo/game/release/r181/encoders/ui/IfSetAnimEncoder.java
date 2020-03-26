package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetAnimMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetAnimEncoder extends MessageEncoder<IfSetAnimMessage> {
	@Override
	public GamePacket encode(IfSetAnimMessage message) {
		final var builder = new GamePacketBuilder(61, PacketType.FIXED);

		builder.put(DataType.SHORT, message.getAnimation());
		builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, message.getPackedInterface());

		return builder.toGamePacket();
	}
}
