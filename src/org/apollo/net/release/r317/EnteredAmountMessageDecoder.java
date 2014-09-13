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
		long amount = reader.getUnsigned(DataType.INT);
		if(amount > Integer.MAX_VALUE) {
			amount = Integer.MAX_VALUE;
		} else if(amount < 0) {
			amount = 0;
		}
		return new EnteredAmountMessage((int) amount);
	}

}