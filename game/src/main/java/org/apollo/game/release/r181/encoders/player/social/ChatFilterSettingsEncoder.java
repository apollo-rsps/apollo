package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.encode.ChatFilterSettingsMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class ChatFilterSettingsEncoder extends MessageEncoder<ChatFilterSettingsMessage> {

	@Override
	public GamePacket encode(ChatFilterSettingsMessage message) {
		var builder = new GamePacketBuilder(80, PacketType.FIXED);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getTradePrivacy().toInteger(true));
		builder.put(DataType.BYTE, DataTransformation.NEGATE, message.getChatPrivacy().toInteger(true));
		return builder.toGamePacket();
	}
}
