package org.apollo.net.release.r377;

import org.apollo.game.message.impl.ThirdItemActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ThirdItemActionMessage}.
 * 
 * @author Graham
 */
public final class ThirdItemActionMessageDecoder extends MessageDecoder<ThirdItemActionMessage> {

	@Override
	public ThirdItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		return new ThirdItemActionMessage(interfaceId, id, slot);
	}

}
