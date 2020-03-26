package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.IfSetTextMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetTextEncoder extends MessageEncoder<IfSetTextMessage> {
	@Override
	public GamePacket encode(IfSetTextMessage message) {
		final var builder = new GamePacketBuilder(19, PacketType.VARIABLE_SHORT);

		builder.put(DataType.INT, message.getPackedInterface());
		builder.putString(message.getText());

		return builder.toGamePacket();
	}
}
