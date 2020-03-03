package org.apollo.game.release.r181;

import org.apollo.game.message.impl.PlayerActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

import java.nio.ByteOrder;

/**
 * A {@link MessageDecoder} for the seventh {@link PlayerActionMessage}.
 *
 * @author Major
 */
public final class SeventhPlayerActionMessageDecoder extends MessageDecoder<PlayerActionMessage> {

	@Override
	public PlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new PlayerActionMessage(7, index, movementType);
	}
}