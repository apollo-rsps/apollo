package org.apollo.net.release.r377;

import org.apollo.game.event.impl.LogoutEvent;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link LogoutEvent}.
 * @author Graham
 */
public final class LogoutEventEncoder extends EventEncoder<LogoutEvent> {

	@Override
	public GamePacket encode(LogoutEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(5);
		return builder.toGamePacket();
	}

}
