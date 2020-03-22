package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.RebuildNormalMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link RebuildNormalMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class RebuildNormalMessageEncoder extends MessageEncoder<RebuildNormalMessage> {

	@Override
	public GamePacket encode(RebuildNormalMessage message) {
		final var builder = new GamePacketBuilder(0, PacketType.VARIABLE_SHORT);
		final var position = message.getPosition();
		// TODO the player shit here.

		if (!message.isHasLastKnownRegion()) {
			builder.switchToBitAccess();
			builder.putBits(30, position.hashCode());
			for (int index = 1; index < 2048; index++) {
				if (index == message.getIndex()) {
					continue;
				}
				builder.putBits(18, 0);
			}
			builder.switchToByteAccess();
		}

		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD,
				message.getPosition().getCentralRegionY());
		builder.put(DataType.SHORT, DataTransformation.ADD, message.getPosition().getCentralRegionX());

		boolean force = true;
		int centralMapX = position.getCentralRegionX() / 8;
		int centralMapY = position.getCentralRegionY() / 8;

		if ((centralMapX == 48 || centralMapX == 49) && centralMapY == 48) force = false;

		if (centralMapX == 48 && centralMapY == 148) force = false;

		GamePacketBuilder map = new GamePacketBuilder();
		for (int mapX = ((position.getCentralRegionX() - 6) / 8); mapX <= ((position
				.getCentralRegionX() + 6) / 8); mapX++) {
			for (int mapY = ((position.getCentralRegionY() - 6) / 8); mapY <= ((position
					.getCentralRegionY() + 6) / 8); mapY++) {
				if (force || (mapY != 49 && mapY != 149 && mapY != 147 && mapX != 50 && (mapX != 49 || mapY != 47))) {
					int[] keys = message.getRepository().get(mapX, mapY);
					for (int i = 0; i < 4; i++)
						map.put(DataType.INT, keys[i]);
				}
			}
		}

		builder.put(DataType.SHORT, map.getLength() / Integer.BYTES);
		builder.putBytes(map);
		return builder.toGamePacket();
	}

}