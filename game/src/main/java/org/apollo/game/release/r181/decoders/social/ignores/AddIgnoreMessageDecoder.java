package org.apollo.game.release.r181.decoders.social.ignores;

import org.apollo.game.message.impl.AddIgnoreMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link AddIgnoreMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class AddIgnoreMessageDecoder extends MessageDecoder<AddIgnoreMessage> {

	@Override
	public AddIgnoreMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = reader.getString();
		return new AddIgnoreMessage(username);
	}

}