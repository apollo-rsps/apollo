package org.apollo.game.release.r377;

import org.apollo.game.message.impl.MouseClickedMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link MouseClickedMessage}
 *
 * @author Stuart
 */
public final class MouseClickedMessageDecoder extends MessageDecoder<MouseClickedMessage> {

	@Override
	public MouseClickedMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int value = (int) reader.getUnsigned(DataType.INT);

		long delay = (value >> 20) * 50;
		boolean right = (value >> 19 & 0x1) == 1;

		int cords = value & 0x3FFFF;
		int x = cords % 765;
		int y = cords / 765;

		return new MouseClickedMessage(delay, right, x, y);
	}

}