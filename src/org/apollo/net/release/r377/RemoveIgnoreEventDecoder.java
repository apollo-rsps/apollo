package org.apollo.net.release.r377;

import org.apollo.game.event.impl.RemoveIgnoreEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventDecoder} for the {@link RemoveIgnoreEvent}.
 * 
 * @author Major
 */
public final class RemoveIgnoreEventDecoder extends EventDecoder<RemoveIgnoreEvent> {

	@Override
	public RemoveIgnoreEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		return new RemoveIgnoreEvent(username);
	}

}