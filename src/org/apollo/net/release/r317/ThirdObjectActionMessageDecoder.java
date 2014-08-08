package org.apollo.net.release.r317;

import org.apollo.game.message.impl.ThirdObjectActionMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ThirdObjectActionMessage}.
 * 
 * @author Graham
 */
public final class ThirdObjectActionMessageDecoder extends MessageDecoder<ThirdObjectActionMessage> {

	@Override
	public ThirdObjectActionMessage decode(GamePacket packet) {
		// TODO ripped out of some Winterlove-based server, so probably wrong
		GamePacketReader reader = new GamePacketReader(packet);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int y = (int) reader.getUnsigned(DataType.SHORT);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new ThirdObjectActionMessage(id, new Position(x, y));
	}

}