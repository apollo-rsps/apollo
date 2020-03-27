package org.apollo.game.release.r181.decoders.ui.if1;

import org.apollo.game.message.impl.decode.IfActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FirstIf1ActionMessageDecoder extends MessageDecoder<IfActionMessage> {
	@Override
	public IfActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int packedInterface = (int) reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE);
		int item = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new IfActionMessage(1, packedInterface >> 16, packedInterface & 0xFFFF, slot, item);
	}
}
