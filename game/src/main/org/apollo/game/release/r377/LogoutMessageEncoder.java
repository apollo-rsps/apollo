package org.apollo.game.release.r377;

import org.apollo.game.message.impl.LogoutMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link LogoutMessage}.
 *
 * @author Graham
 */
public final class LogoutMessageEncoder extends MessageEncoder<LogoutMessage> {

	@Override
	public GamePacket encode(LogoutMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(5);
		return builder.toGamePacket();
	}

}