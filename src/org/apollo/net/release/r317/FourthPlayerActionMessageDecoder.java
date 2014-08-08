package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FourthPlayerActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FourthPlayerActionMessage}.
 * 
 * @author Major
 */
public final class FourthPlayerActionMessageDecoder extends MessageDecoder<FourthPlayerActionMessage> {

	@Override
	public FourthPlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new FourthPlayerActionMessage(index);
	}

}