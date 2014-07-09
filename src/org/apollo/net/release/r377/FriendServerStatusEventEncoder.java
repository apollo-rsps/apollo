package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FriendServerStatusEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link FriendServerStatusEvent}.
 * 
 * @author Major
 */
public final class FriendServerStatusEventEncoder extends EventEncoder<FriendServerStatusEvent> {

    @Override
    public GamePacket encode(FriendServerStatusEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(251);
	builder.put(DataType.BYTE, event.getStatusCode());
	return builder.toGamePacket();
    }
}