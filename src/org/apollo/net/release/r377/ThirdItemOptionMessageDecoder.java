package org.apollo.net.release.r377;

import org.apollo.game.message.impl.ThirdItemOptionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ThirdItemOptionMessage}.
 * 
 * @author Chris Fletcher
 */
final class ThirdItemOptionMessageDecoder extends MessageDecoder<ThirdItemOptionMessage> {

	@Override
	public ThirdItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new ThirdItemOptionMessage(interfaceId, id, slot);
	}

}
