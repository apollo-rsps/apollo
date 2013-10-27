package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ServerMessageEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link ServerMessageEvent}.
 * @author Graham
 */
public final class ServerMessageEventEncoder extends EventEncoder<ServerMessageEvent> {

	@Override
	public GamePacket encode(ServerMessageEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(253, PacketType.VARIABLE_BYTE);
		builder.putString(event.getMessage());
		return builder.toGamePacket();
	}

}
