package org.apollo.game.release.r181.decoders.ui;

import org.apollo.game.message.impl.decode.DisplayStatusMessage;
import org.apollo.game.model.inter.DisplayMode;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

public class DisplayStatusMessageDecoder extends MessageDecoder<DisplayStatusMessage> {
	@Override
	public DisplayStatusMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		DisplayMode mode = reader.getUnsigned(DataType.BYTE) == 1 ? DisplayMode.FIXED : DisplayMode.RESIZABLE;
		int width = (int) reader.getUnsigned(DataType.SHORT);
		int height = (int) reader.getUnsigned(DataType.SHORT);
		return new DisplayStatusMessage(mode, height, width);
	}
}
