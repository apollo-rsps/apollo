package org.apollo.game.release.r377;

import org.apollo.game.message.impl.FlashingTabClickedMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FlashingTabClickedMessage}.
 *
 * @author Major
 */
public final class FlashingTabClickedMessageDecoder extends MessageDecoder<FlashingTabClickedMessage> {

	@Override
	public FlashingTabClickedMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int tab = (int) reader.getUnsigned(DataType.BYTE);
		return new FlashingTabClickedMessage(tab);
	}

}