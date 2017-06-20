package org.apollo.game.release.r377;

import org.apollo.game.message.impl.SendFriendMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;
import org.apollo.util.NameUtil;

/**
 * A {@link MessageEncoder} for the {@link SendFriendMessage}.
 *
 * @author Major
 */
public final class SendFriendMessageEncoder extends MessageEncoder<SendFriendMessage> {

	@Override
	public GamePacket encode(SendFriendMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(78);
		builder.put(DataType.LONG, NameUtil.encodeBase37(message.getUsername()));
		builder.put(DataType.BYTE, message.getEncodedWorld());
		return builder.toGamePacket();
	}

}