package org.apollo.net.release.r377;

import org.apollo.game.message.impl.FirstPlayerActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FirstPlayerActionMessage}.
 * 
 * @author Major
 */
public final class FirstPlayerActionMessageDecoder extends MessageDecoder<FirstPlayerActionMessage> {

	@Override
	public FirstPlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new FirstPlayerActionMessage(index);
	}

}