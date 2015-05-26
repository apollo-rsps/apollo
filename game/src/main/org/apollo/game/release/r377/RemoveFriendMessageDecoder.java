package org.apollo.game.release.r377;

import org.apollo.game.message.impl.RemoveFriendMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageDecoder} for the {@link RemoveFriendMessage}.
 *
 * @author Major
 */
public final class RemoveFriendMessageDecoder extends MessageDecoder<RemoveFriendMessage> {

	@Override
	public RemoveFriendMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		String username = NameUtil.decodeBase37(reader.getSigned(DataType.LONG));
		return new RemoveFriendMessage(username);
	}

}