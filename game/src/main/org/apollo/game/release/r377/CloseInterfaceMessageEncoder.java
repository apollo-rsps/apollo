package org.apollo.game.release.r377;

import org.apollo.game.message.impl.CloseInterfaceMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link CloseInterfaceMessage}.
 *
 * @author Graham
 */
public final class CloseInterfaceMessageEncoder extends MessageEncoder<CloseInterfaceMessage> {

	@Override
	public GamePacket encode(CloseInterfaceMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(29);
		return builder.toGamePacket();
	}

}