package org.apollo.game.release.r181.decoders.map.player.actions;

import org.apollo.game.message.impl.decode.PlayerActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the first {@link PlayerActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class FirstPlayerActionMessageDecoder extends MessageDecoder<PlayerActionMessage> {

	@Override
	public PlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.NEGATE);
		return new PlayerActionMessage(1, index, movementType);
	}
}