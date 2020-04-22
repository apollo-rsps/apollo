package org.apollo.game.release.r181.encoders.player.social;

import org.apollo.game.message.impl.UpdateIgnoreListMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public final class UpdateIgnoreListEncoder extends MessageEncoder<UpdateIgnoreListMessage> {

	@Override
	public GamePacket encode(UpdateIgnoreListMessage message) {
		final var builder = new GamePacketBuilder(32, PacketType.VARIABLE_SHORT);
		final var components = message.getComponents();

		for (var component : components) {
			builder.put(DataType.BYTE, 0x0); // If you're updating this player or something
			builder.putString(component.getUsername()); // Display name
			builder.putString(""); // Previous display name
			builder.putString("");// Notes, not used in osrs
		}

		return builder.toGamePacket();
	}

}