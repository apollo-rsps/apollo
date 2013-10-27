package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ThirdObjectActionEvent;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ThirdObjectActionEvent}.
 * @author Graham
 */
public final class ThirdObjectActionEventDecoder extends EventDecoder<ThirdObjectActionEvent> {

	@Override
	public ThirdObjectActionEvent decode(GamePacket packet) {
		// TODO ripped out of some Winterlove-based server, so probably wrong
		GamePacketReader reader = new GamePacketReader(packet);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int y = (int) reader.getUnsigned(DataType.SHORT);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new ThirdObjectActionEvent(id, new Position(x, y));
	}

}
