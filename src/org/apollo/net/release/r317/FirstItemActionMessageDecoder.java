package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FirstItemActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FirstItemActionMessage}.
 * 
 * @author Graham
 */
public final class FirstItemActionMessageDecoder extends MessageDecoder<FirstItemActionMessage> {

	@Override
	public FirstItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new FirstItemActionMessage(interfaceId, id, slot);
	}

}