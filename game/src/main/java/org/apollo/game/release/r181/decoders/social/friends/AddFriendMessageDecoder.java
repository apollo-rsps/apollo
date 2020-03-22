package org.apollo.game.release.r181.decoders.social.friends;

import org.apollo.game.message.impl.AddFriendMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link AddFriendMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class AddFriendMessageDecoder extends MessageDecoder<AddFriendMessage> {

	@Override
	public AddFriendMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = reader.getString();
		return new AddFriendMessage(username);
	}

}