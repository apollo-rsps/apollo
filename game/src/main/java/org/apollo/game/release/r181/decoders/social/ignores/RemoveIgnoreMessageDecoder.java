package org.apollo.game.release.r181.decoders.social.ignores;

import org.apollo.game.message.impl.decode.RemoveIgnoreMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link RemoveIgnoreMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class RemoveIgnoreMessageDecoder extends MessageDecoder<RemoveIgnoreMessage> {

	@Override
	public RemoveIgnoreMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = reader.getString();
		return new RemoveIgnoreMessage(username);
	}

}