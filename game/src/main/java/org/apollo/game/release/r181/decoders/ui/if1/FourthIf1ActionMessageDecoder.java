package org.apollo.game.release.r181.decoders.ui.if1;

import org.apollo.game.message.impl.decode.IfActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FourthIf1ActionMessageDecoder extends MessageDecoder<IfActionMessage> {
	@Override
	public IfActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int packedInterface = (int) reader.getUnsigned(DataType.INT);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int item = (int) reader.getUnsigned(DataType.SHORT);

		return new IfActionMessage(4, packedInterface >> 16, packedInterface & 0xFFFF, slot, item);
	}
}
