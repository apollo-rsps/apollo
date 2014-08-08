package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FourthItemOptionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FourthItemOptionMessage}.
 * 
 * @author Chris Fletcher
 */
final class FourthItemOptionMessageDecoder extends MessageDecoder<FourthItemOptionMessage> {

	@Override
	public FourthItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new FourthItemOptionMessage(interfaceId, id, slot);
	}

}
