package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FirstNpcActionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FirstNpcActionEvent}.
 * 
 * @author Major
 */
public final class FirstNpcActionEventDecoder extends EventDecoder<FirstNpcActionEvent> {

    @Override
    public FirstNpcActionEvent decode(GamePacket packet) {
	GamePacketReader reader = new GamePacketReader(packet);
	int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
	return new FirstNpcActionEvent(index);
    }

}