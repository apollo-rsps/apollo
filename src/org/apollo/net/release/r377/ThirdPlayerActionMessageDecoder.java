package org.apollo.net.release.r377;

import org.apollo.game.message.impl.ThirdPlayerActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ThirdPlayerActionMessage}.
 * 
 * @author Major
 */
public final class ThirdPlayerActionMessageDecoder extends MessageDecoder<ThirdPlayerActionMessage> {

	@Override
	public ThirdPlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new ThirdPlayerActionMessage(index);
	}

}