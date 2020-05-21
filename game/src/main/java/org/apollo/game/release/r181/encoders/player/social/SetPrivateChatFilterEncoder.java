package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.encode.SetPrivateChatFilterMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class SetPrivateChatFilterEncoder extends MessageEncoder<SetPrivateChatFilterMessage> {
	@Override
	public GamePacket encode(SetPrivateChatFilterMessage message) {
		final var builder = new GamePacketBuilder(68, PacketType.FIXED);
		builder.put(DataType.BYTE, message.getFriendPrivacy().toInteger(true));
		return builder.toGamePacket();
	}
}
