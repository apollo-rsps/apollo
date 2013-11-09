package org.apollo.net.release.r317;

import org.apollo.game.event.impl.TakeTileItemEvent;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link TakeTileItemEvent}.
 * 
 * @author Major
 */
public class TakeTileItemEventDecoder extends EventDecoder<TakeTileItemEvent> {

	@Override
	public TakeTileItemEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new TakeTileItemEvent(id, new Position(x, y));
	}

}