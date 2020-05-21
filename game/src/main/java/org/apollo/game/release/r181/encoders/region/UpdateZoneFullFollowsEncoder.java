package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.UpdateZoneFullFollowsMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class UpdateZoneFullFollowsEncoder extends MessageEncoder<UpdateZoneFullFollowsMessage> {
	@Override
	public GamePacket encode(UpdateZoneFullFollowsMessage message) {
		final var builder = new GamePacketBuilder(25, PacketType.FIXED);

		final var player = message.getPlayerPosition();
		final var region = message.getRegionPosition();

		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalY(player));
		builder.put(DataType.BYTE, region.getLocalX(player));

		return builder.toGamePacket();
	}
}
