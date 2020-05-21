package org.apollo.game.release.r181.decoders.map.item;

import org.apollo.game.message.impl.decode.OpItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class ThirdOpItemMessageDecoder extends MessageDecoder<OpItemMessage> {
	@Override
	public OpItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int movementType = (int) reader.getUnsigned(DataType.BYTE, DataTransformation.SUBTRACT);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new OpItemMessage(3, id, x, y, movementType);
	}
}
