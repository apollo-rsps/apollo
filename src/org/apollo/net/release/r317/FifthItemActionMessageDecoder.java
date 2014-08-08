package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FifthItemActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FifthItemActionMessage}.
 * 
 * @author Graham
 */
public final class FifthItemActionMessageDecoder extends MessageDecoder<FifthItemActionMessage> {

	@Override
	public FifthItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new FifthItemActionMessage(interfaceId, id, slot);
	}

}