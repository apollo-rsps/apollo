package org.apollo.net.release.r377;

import org.apollo.game.message.impl.FourthItemActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FourthItemActionMessage}.
 * 
 * @author Graham
 */
public final class FourthItemActionMessageDecoder extends MessageDecoder<FourthItemActionMessage> {

	@Override
	public FourthItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT);
		return new FourthItemActionMessage(interfaceId, id, slot);
	}

}