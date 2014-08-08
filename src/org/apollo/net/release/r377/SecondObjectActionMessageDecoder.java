package org.apollo.net.release.r377;

import org.apollo.game.message.impl.SecondObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SecondObjectActionMessage}.
 * 
 * @author Graham
 */
public final class SecondObjectActionMessageDecoder extends MessageDecoder<SecondObjectActionMessage> {

	@Override
	public SecondObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int id = (int) reader.getUnsigned(DataType.SHORT);
		int x = (int) reader.getUnsigned(DataType.SHORT);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new SecondObjectActionMessage(id, new Position(x, y));
	}

}