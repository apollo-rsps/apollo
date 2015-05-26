package org.apollo.game.release.r377;

import org.apollo.game.message.impl.EnteredAmountMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link EnteredAmountMessage}.
 *
 * @author Graham
 */
public final class EnteredAmountMessageDecoder extends MessageDecoder<EnteredAmountMessage> {

	@Override
	public EnteredAmountMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int amount = (int) reader.getUnsigned(DataType.INT);
		return new EnteredAmountMessage(amount);
	}

}