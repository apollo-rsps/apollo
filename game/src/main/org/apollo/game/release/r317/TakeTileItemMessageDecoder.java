package org.apollo.game.release.r317;

import org.apollo.game.message.impl.TakeTileItemMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link TakeTileItemMessage}.
 *
 * @author Major
 */
public final class TakeTileItemMessageDecoder extends MessageDecoder<TakeTileItemMessage> {

	@Override
	public TakeTileItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new TakeTileItemMessage(id, new Position(x, y));
	}

}