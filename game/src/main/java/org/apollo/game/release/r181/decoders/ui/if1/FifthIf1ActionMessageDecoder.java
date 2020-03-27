package org.apollo.game.release.r181.decoders.ui.if1;

import org.apollo.game.message.impl.decode.IfActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FifthIf1ActionMessageDecoder extends MessageDecoder<IfActionMessage> {
	@Override
	public IfActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int item = (int) reader.getUnsigned(DataType.SHORT);
		int packedInterface = (int) reader.getUnsigned(DataType.INT, DataOrder.MIDDLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT);

		return new IfActionMessage(5, packedInterface >> 16, packedInterface & 0xFFFF, slot, item);
	}
}
