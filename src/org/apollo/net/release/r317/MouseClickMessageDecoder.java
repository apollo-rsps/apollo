package org.apollo.net.release.r317;

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
		int read;
		if (reader.getLength() == 2) {
			read = (int) reader.getUnsigned(DataType.SHORT);
			int clickCount = (read >> 12);
			int dX = (read >> 6) & 0x3f;
			int dY = read & 0x3f;
			return new MouseClickMessage(clickCount, dX, dY, true);
		} else if (reader.getLength() == 3) {
			read = (int) reader.getUnsigned(DataType.TRI_BYTE) & ~0x800000;
		} else {
			read = (int) reader.getUnsigned(DataType.INT) & ~0xc0000000;
		}
		int clickCount = (read >> 19);
		int x = (read & 0x7f) % 765;
		int y = (read & 0x7f) / 765;
		return new MouseClickMessage(clickCount, x, y, false);
	}

}