package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SecondNpcActionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link SecondNpcActionEvent}.
 * 
 * @author Major
 */
public final class SecondNpcActionEventDecoder extends EventDecoder<SecondNpcActionEvent> {

	@Override
	public SecondNpcActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
        int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new SecondNpcActionEvent(index);
	}

}