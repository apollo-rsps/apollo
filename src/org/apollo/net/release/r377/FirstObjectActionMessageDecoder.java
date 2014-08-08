package org.apollo.net.release.r377;

import org.apollo.game.message.impl.FirstObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FirstObjectActionMessage}.
 * 
 * @author Graham
 */
public final class FirstObjectActionMessageDecoder extends MessageDecoder<FirstObjectActionMessage> {

	@Override
	public FirstObjectActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new FirstObjectActionMessage(id, new Position(x, y));
	}

}