package org.apollo.game.release.r181.decoders.map.player.actions;

import org.apollo.game.message.impl.PlayerActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the eight {@link PlayerActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class EightPlayerActionMessageDecoder extends MessageDecoder<PlayerActionMessage> {

	@Override
	public PlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.NEGATE);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new PlayerActionMessage(8, index, movementType);
	}
}