package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.SpotAnimMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class SpotAnimEncoder extends MessageEncoder<SpotAnimMessage> {
	@Override
	public GamePacket encode(SpotAnimMessage message) {
		final var spotAnim = message.getSpotAnim();
		final var builder = new GamePacketBuilder(8, PacketType.FIXED);
		builder.put(DataType.SHORT, spotAnim.getId());
		builder.put(DataType.BYTE, DataTransformation.ADD, spotAnim.getHeight());
		builder.put(DataType.SHORT, DataTransformation.ADD, spotAnim.getDelay());
		builder.put(DataType.SHORT, message.getPositionOffset());
		return builder.toGamePacket();
	}
}
