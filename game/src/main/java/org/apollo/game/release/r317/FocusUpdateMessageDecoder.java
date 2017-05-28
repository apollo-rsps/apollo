package org.apollo.game.release.r317;

import org.apollo.game.message.impl.FocusUpdateMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FocusUpdateMessage}.
 *
 * @author Major
 */
public final class FocusUpdateMessageDecoder extends MessageDecoder<FocusUpdateMessage> {

	@Override
	public FocusUpdateMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		boolean focused = (byte) reader.getUnsigned(DataType.BYTE) == 1;
		return new FocusUpdateMessage(focused);
	}

}