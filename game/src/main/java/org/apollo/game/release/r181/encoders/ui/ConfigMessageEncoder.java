package org.apollo.game.release.r181.encoders.ui;

import org.apollo.game.message.impl.encode.ConfigMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link ConfigMessage}.
 *
 * @author Chris Fletcher
 * @author Khaled Abdeljaber
 */
public final class ConfigMessageEncoder extends MessageEncoder<ConfigMessage> {

	@Override
	public GamePacket encode(ConfigMessage message) {
		GamePacketBuilder builder;
		int value = message.getValue();

		if (value > Byte.MIN_VALUE && value < Byte.MAX_VALUE) {
			builder = new GamePacketBuilder(63);

			builder.put(DataType.BYTE, DataTransformation.ADD, value & 0xFF);
			builder.put(DataType.SHORT, DataTransformation.ADD, message.getId());
		} else {
			builder = new GamePacketBuilder(4);

			builder.put(DataType.SHORT, message.getId());
			builder.put(DataType.INT, value);
		}

		return builder.toGamePacket();
	}

}