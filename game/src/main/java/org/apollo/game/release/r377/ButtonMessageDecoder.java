package org.apollo.game.release.r377;

import org.apollo.game.message.impl.ButtonMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ButtonMessage}.
 *
 * @author Graham
 */
public final class ButtonMessageDecoder extends MessageDecoder<ButtonMessage> {

	@Override
	public ButtonMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		return new ButtonMessage(interfaceId);
	}

}