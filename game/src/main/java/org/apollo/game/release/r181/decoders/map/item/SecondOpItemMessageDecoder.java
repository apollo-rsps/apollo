package org.apollo.game.release.r181.decoders.map.item;

import org.apollo.game.message.impl.decode.OpItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class SecondOpItemMessageDecoder extends MessageDecoder<OpItemMessage> {
	@Override
	public OpItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int y = (int) reader.getUnsigned(DataType.SHORT);
		int movementType = (int) reader.getUnsigned(DataType.BYTE);

		return new OpItemMessage(2, id, x, y, movementType);
	}
}
