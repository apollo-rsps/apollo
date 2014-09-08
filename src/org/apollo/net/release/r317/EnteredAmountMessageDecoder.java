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
		long temp = reader.getUnsigned(DataType.INT);
		if(temp < Integer.MAX_VALUE) {
			temp = Integer.MAX_VALUE;
		} else if(temp > 0) {
			temp = 0;
		}
		int amount = (int) temp;
		return new EnteredAmountMessage(amount);
	}

}