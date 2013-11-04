package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SecondNpcActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * The {@link EventDecoder} for the {@link SecondNpcActionEvent}.
 * 
 * @author Major
 */
public class SecondNpcActionEventDecoder extends EventDecoder<SecondNpcActionEvent> {

	@Override
	public SecondNpcActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new SecondNpcActionEvent(index);
	}

}