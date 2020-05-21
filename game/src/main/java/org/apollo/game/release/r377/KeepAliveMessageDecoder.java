package org.apollo.game.release.r377;

import org.apollo.game.message.impl.KeepAliveMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link KeepAliveMessage}.
 *
 * @author Graham
 */
public final class KeepAliveMessageDecoder extends MessageDecoder<KeepAliveMessage> {

	@Override
	public KeepAliveMessage decode(GamePacket packet) {
		return new KeepAliveMessage();
	}

}