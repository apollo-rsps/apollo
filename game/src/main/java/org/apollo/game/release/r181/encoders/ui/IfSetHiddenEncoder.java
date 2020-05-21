package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetHideMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetHiddenEncoder extends MessageEncoder<IfSetHideMessage> {
	@Override
	public GamePacket encode(IfSetHideMessage message) {
		final var builder = new GamePacketBuilder(21, PacketType.FIXED);

		builder.put(DataType.INT, message.getPackedInterface());
		builder.put(DataType.BYTE, DataOrder.INVERSED_MIDDLE, message.isVisible() ? 0 : 1);

		return builder.toGamePacket();
	}
}
