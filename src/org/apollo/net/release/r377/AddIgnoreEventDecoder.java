package org.apollo.net.release.r377;

import org.apollo.game.event.impl.AddIgnoreEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventDecoder} for the {@link AddIgnoreEvent}.
 * 
 * @author Major
 */
public final class AddIgnoreEventDecoder extends EventDecoder<AddIgnoreEvent> {

	@Override
	public AddIgnoreEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		return new AddIgnoreEvent(username);
	}

}