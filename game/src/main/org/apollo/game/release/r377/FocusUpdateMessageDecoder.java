package org.apollo.game.release.r377;

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
		GamePacketReader decoder = new GamePacketReader(packet);
		return new FocusUpdateMessage(decoder.getUnsigned(DataType.BYTE) == 1);
	}

}