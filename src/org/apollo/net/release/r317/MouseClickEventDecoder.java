package org.apollo.net.release.r317;

import org.apollo.game.event.impl.MouseClickEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link MouseClickEvent}.
 * 
 * @author Major
 */
public class MouseClickEventDecoder extends EventDecoder<MouseClickEvent> {

	@Override
	public MouseClickEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		reader.getUnsigned(DataType.INT);
		return new MouseClickEvent();
	}

}