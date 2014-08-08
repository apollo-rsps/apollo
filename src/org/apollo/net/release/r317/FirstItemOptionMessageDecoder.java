package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FirstItemOptionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FirstItemOptionMessage}.
 * 
 * @author Graham
 */
final class FirstItemOptionMessageDecoder extends MessageDecoder<FirstItemOptionMessage> {

	@Override
	public FirstItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);

		return new FirstItemOptionMessage(interfaceId, id, slot);
	}

}