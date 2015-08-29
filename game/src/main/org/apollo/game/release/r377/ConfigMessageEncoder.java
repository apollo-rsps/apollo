package org.apollo.game.release.r377;

import org.apollo.game.message.impl.ConfigMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link ConfigMessage}.
 *
 * @author Chris Fletcher
 * @author Major
 */
public final class ConfigMessageEncoder extends MessageEncoder<ConfigMessage> {

	@Override
	public GamePacket encode(ConfigMessage message) {
		GamePacketBuilder builder;
		int value = message.getValue();

		if (value > Byte.MIN_VALUE && value < Byte.MAX_VALUE) {
			builder = new GamePacketBuilder(182);

			builder.put(DataType.SHORT, DataTransformation.ADD, message.getId());
			builder.put(DataType.BYTE, DataTransformation.SUBTRACT, value & 0xFF);
		} else {
			builder = new GamePacketBuilder(115);

			builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, value);
			builder.put(DataType.SHORT, DataOrder.LITTLE, message.getId());
		}

		return builder.toGamePacket();
	}

}