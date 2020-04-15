package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.AreaSoundMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * @author Khaled Abdeljaber
 */
public class AreaSoundEncoder extends MessageEncoder<AreaSoundMessage> {
	@Override
	public GamePacket encode(AreaSoundMessage message) {
		final var sound = message.getSound();
		final var builder = new GamePacketBuilder(12, PacketType.FIXED);
		builder.put(DataType.BYTE, sound.getLength() << 4 | sound.getVolume());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, sound.getDelay());
		builder.put(DataType.BYTE, DataTransformation.ADD, message.getPositionOffset());
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, sound.getId());
		return builder.toGamePacket();
	}
}
