package org.apollo.game.release.r181.decoders;

import org.apollo.game.message.impl.decode.ArrowKeyMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ArrowKeyMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class ArrowKeyMessageDecoder extends MessageDecoder<ArrowKeyMessage> {

	@Override
	public ArrowKeyMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int roll = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int yaw = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new ArrowKeyMessage(roll, yaw);
	}
}