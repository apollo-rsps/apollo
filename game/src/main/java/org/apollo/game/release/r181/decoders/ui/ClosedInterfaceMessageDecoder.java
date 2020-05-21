package org.apollo.game.release.r181.decoders.ui;

import org.apollo.game.message.impl.decode.ClosedInterfaceMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ClosedInterfaceMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class ClosedInterfaceMessageDecoder extends MessageDecoder<ClosedInterfaceMessage> {

	@Override
	public ClosedInterfaceMessage decode(GamePacket packet) {
		return new ClosedInterfaceMessage();
	}

}