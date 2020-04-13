package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.UpdateZonePartialFollowsMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class UpdateZonePartialFollowsEncoder extends MessageEncoder<UpdateZonePartialFollowsMessage> {
	@Override
	public GamePacket encode(UpdateZonePartialFollowsMessage message) {
		final var builder = new GamePacketBuilder(64, PacketType.FIXED);

		final var player = message.getPlayerPosition();
		final var region = message.getRegionPosition();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalX(player));
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, region.getLocalY(player));

		return builder.toGamePacket();
	}
}
