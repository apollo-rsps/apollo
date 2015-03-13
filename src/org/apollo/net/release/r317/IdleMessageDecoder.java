package org.apollo.net.release.r317;

import org.apollo.game.message.impl.IdleMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link IdleMessage}.
 * 
 * @author Lmctruck30
 */
public final class IdleMessageDecoder extends MessageDecoder<IdleMessage> {

	public IdleMessage decode(GamePacket packet) {
		return new IdleMessage();
	}

}
