package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ThirdPlayerActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ThirdPlayerActionEvent}.
 * 
 * @author Major
 */
public final class ThirdPlayerActionEventDecoder extends EventDecoder<ThirdPlayerActionEvent> {

    @Override
    public ThirdPlayerActionEvent decode(GamePacket packet) {
	GamePacketReader reader = new GamePacketReader(packet);
	int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
	return new ThirdPlayerActionEvent(index);
    }

}