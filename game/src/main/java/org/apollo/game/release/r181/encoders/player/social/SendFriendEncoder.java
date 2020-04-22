package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.SendFriendMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public final class SendFriendEncoder extends MessageEncoder<SendFriendMessage> {

	@Override
	public GamePacket encode(SendFriendMessage message) {
		final var builder = new GamePacketBuilder(58, PacketType.VARIABLE_SHORT);
		final var components = message.getComponents();

		for (var component : components) {
			builder.put(DataType.BYTE, 0); // Friend Chat Rank
			builder.putString(component.getUsername()); // Displayname
			builder.putString(""); // Previous Displayname
			builder.put(DataType.SHORT, component.getEncodedWorld());
			builder.put(DataType.BYTE, 0); // Friend Chat Rank
			builder.put(DataType.BYTE, 0); // Some set of attributes, sorts the list somehow.
			builder.putString(""); // Note
		}

		return builder.toGamePacket();
	}

}