package org.apollo.net.release.r377;

import org.apollo.game.event.impl.TakeTileItemEvent;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link TakeTileItemEvent}.
 * 
 * @author Major
 */
public final class TakeTileItemEventDecoder extends EventDecoder<TakeTileItemEvent> {

	@Override
	public TakeTileItemEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new TakeTileItemEvent(id, new Position(x, y));
	}

}