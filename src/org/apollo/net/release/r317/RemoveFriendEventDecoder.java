package org.apollo.net.release.r317;

import org.apollo.game.event.impl.RemoveFriendEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventDecoder} for the {@link RemoveFriendEvent}.
 * 
 * @author Major
 */
public final class RemoveFriendEventDecoder extends EventDecoder<RemoveFriendEvent> {

	@Override
	public RemoveFriendEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		return new RemoveFriendEvent(username);
	}

}