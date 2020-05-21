package org.apollo.game.release.r181.decoders;

import org.apollo.game.message.impl.decode.KeepAliveMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link KeepAliveMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class KeepAliveMessageDecoder extends MessageDecoder<KeepAliveMessage> {

	@Override
	public KeepAliveMessage decode(GamePacket packet) {
		return new KeepAliveMessage();
	}

}