package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.encode.MessagePrivateEchoMessage;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link MessagePrivateEchoMessage}.
 *
 * @author Major
 */
public final class MessagePrivateEchoEncoder extends MessageEncoder<MessagePrivateEchoMessage> {

	@Override
	public GamePacket encode(MessagePrivateEchoMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(38, PacketType.VARIABLE_SHORT);

		builder.putString(message.getUsername());
		builder.putBytes(message.getMessage());

		return builder.toGamePacket();
	}

}