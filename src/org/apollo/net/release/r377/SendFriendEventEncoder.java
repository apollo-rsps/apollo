package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SendFriendEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;
import org.apollo.util.NameUtil;

/**
 * An {@link EventEncoder} for the {@link SendFriendEvent}.
 * 
 * @author Major
 */
public final class SendFriendEventEncoder extends EventEncoder<SendFriendEvent> {

	@Override
	public GamePacket encode(SendFriendEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(78);
		builder.put(DataType.LONG, NameUtil.encodeBase37(event.getUsername()));
		builder.put(DataType.BYTE, event.getWorld());
		return builder.toGamePacket();
	}

}