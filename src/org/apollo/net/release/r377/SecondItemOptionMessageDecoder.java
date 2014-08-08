package org.apollo.net.release.r377;

import org.apollo.game.message.impl.SecondItemOptionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SecondItemOptionMessage}.
 * 
 * @author Graham
 */
final class SecondItemOptionMessageDecoder extends MessageDecoder<SecondItemOptionMessage> {

	@Override
	public SecondItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new SecondItemOptionMessage(interfaceId, id, slot);
	}

}