package org.apollo.net.release.r317;

import org.apollo.game.message.impl.EnteredAmountMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link EnteredAmountMessage}.
 * 
 * @author Graham
 * @author Lmctruck30
 */
public final class EnteredAmountMessageDecoder extends MessageDecoder<EnteredAmountMessage> {

	@Override
	public EnteredAmountMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int amount = (int) reader.getSigned(DataType.INT);
		return new EnteredAmountMessage(amount);
	}

}