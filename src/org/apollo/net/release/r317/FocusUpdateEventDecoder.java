package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FocusUpdateEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FocusUpdateEvent}.
 * 
 * @author Major
 */
public class FocusUpdateEventDecoder extends EventDecoder<FocusUpdateEvent> {

	@Override
	public FocusUpdateEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		boolean focused = (byte) reader.getUnsigned(DataType.BYTE) == 1;
		return new FocusUpdateEvent(focused);
	}

}