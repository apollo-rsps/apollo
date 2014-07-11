package org.apollo.net.release.r317;

import org.apollo.game.event.impl.AddFriendEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventDecoder} for the {@link AddFriendEvent}.
 * 
 * @author Major
 */
public final class AddFriendEventDecoder extends EventDecoder<AddFriendEvent> {

	@Override
	public AddFriendEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		return new AddFriendEvent(username);
	}

}