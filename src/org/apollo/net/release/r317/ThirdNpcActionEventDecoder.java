package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ThirdNpcActionEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ThirdNpcActionEvent}.
 * 
 * @author Major
 */
public final class ThirdNpcActionEventDecoder extends EventDecoder<ThirdNpcActionEvent> {

	@Override
	public ThirdNpcActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
        int index = (int) reader.getSigned(DataType.SHORT);
		return new ThirdNpcActionEvent(index);
	}

}