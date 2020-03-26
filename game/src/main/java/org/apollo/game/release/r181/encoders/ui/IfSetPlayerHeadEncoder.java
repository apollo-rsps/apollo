package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetItemMessage;
import org.apollo.game.message.impl.encode.IfSetPlayerHeadMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetPlayerHeadEncoder extends MessageEncoder<IfSetPlayerHeadMessage> {
	@Override
	public GamePacket encode(IfSetPlayerHeadMessage message) {
		final var builder = new GamePacketBuilder(30, PacketType.FIXED);

		builder.put(DataType.INT, message.getPackedInterface());

		return builder.toGamePacket();
	}
}
