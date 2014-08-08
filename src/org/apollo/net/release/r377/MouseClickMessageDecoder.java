package org.apollo.net.release.r377;

import org.apollo.game.message.impl.MouseClickMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link MouseClickMessage}.
 * 
 * @author Major
 */
public final class MouseClickMessageDecoder extends MessageDecoder<MouseClickMessage> {

	@Override
	public MouseClickMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int read, clickCount, x, y;

		if (reader.getLength() == 2) {
			read = (int) reader.getUnsigned(DataType.SHORT);
			clickCount = (read >> 12);
			x = (read >> 6) & 0x3f;
			y = read & 0x3f;
			return new MouseClickMessage(clickCount, x, y, true);
		} else if (reader.getLength() == 3) {
			read = (int) reader.getUnsigned(DataType.TRI_BYTE) & ~0x800000;
		} else {
			read = (int) reader.getUnsigned(DataType.INT) & ~0xc0000000;
		}
		clickCount = (read >> 19);
		x = (read & 0x7f) % 765;
		y = (read & 0x7f) / 765;
		return new MouseClickMessage(clickCount, x, y, false);
	}

}