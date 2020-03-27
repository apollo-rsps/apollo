package org.apollo.game.release.r181.decoders.map.player.actions;

import org.apollo.game.message.impl.decode.PlayerActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the fifth {@link PlayerActionMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class FifthPlayerActionMessageDecoder extends MessageDecoder<PlayerActionMessage> {

	@Override
	public PlayerActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.ADD);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new PlayerActionMessage(5, index, movementType);
	}
}