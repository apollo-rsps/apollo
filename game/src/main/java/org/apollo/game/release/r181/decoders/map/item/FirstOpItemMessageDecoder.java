package org.apollo.game.release.r181.decoders.map.item;

import org.apollo.game.message.impl.decode.OpItemMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FirstOpItemMessageDecoder extends MessageDecoder<OpItemMessage> {
	@Override
	public OpItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int movementType = (int) reader.getUnsigned(DataType.BYTE);

		return new OpItemMessage(1, id, x, y, movementType);
	}
}
