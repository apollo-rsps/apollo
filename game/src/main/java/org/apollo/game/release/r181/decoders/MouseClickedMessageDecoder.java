package org.apollo.game.release.r181.decoders;

import org.apollo.game.message.impl.decode.MouseClickedMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link MouseClickedMessage}
 *
 * @author Khaled Abdeljaber
 */
public final class MouseClickedMessageDecoder extends MessageDecoder<MouseClickedMessage> {

	@Override
	public MouseClickedMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int mousePacked = (int) reader.getUnsigned(DataType.SHORT);
		int y = (int) reader.getUnsigned(DataType.SHORT);
		int x = (int) reader.getUnsigned(DataType.SHORT);

		long delay = mousePacked >> 1;
		boolean right = (mousePacked & 0x1) == 1;

		return new MouseClickedMessage(delay, right, x, y);
	}

}