package org.apollo.game.release.r317;

import org.apollo.game.message.impl.FlaggedMouseEventMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link org.apollo.game.message.impl.FlaggedMouseEventMessage}.
 *
 * @author Major
 */
public final class FlaggedMouseEventMessageDecoder extends MessageDecoder<FlaggedMouseEventMessage> {

	@Override
	public FlaggedMouseEventMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int read;
		if (reader.getLength() == 2) {
			read = (int) reader.getUnsigned(DataType.SHORT);
			int clicks = read >> 12;
			int dX = read >> 6 & 0x3f;
			int dY = read & 0x3f;
			return new FlaggedMouseEventMessage(clicks, dX, dY, true);
		} else if (reader.getLength() == 3) {
			read = (int) reader.getUnsigned(DataType.TRI_BYTE) & ~0x800000;
		} else {
			read = (int) reader.getUnsigned(DataType.INT) & ~0xc0000000;
		}

		int clicks = read >> 19;
		int x = (read & 0x7f) % 765;
		int y = (read & 0x7f) / 765;

		return new FlaggedMouseEventMessage(clicks, x, y, false);
	}

}