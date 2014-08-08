package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FifthItemOptionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FifthItemOptionMessage}.
 * 
 * @author Chris Fletcher
 */
final class FifthItemOptionMessageDecoder extends MessageDecoder<FifthItemOptionMessage> {

	@Override
	public FifthItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new FifthItemOptionMessage(interfaceId, id, slot);
	}

}
