package org.apollo.game.release.r181.decoders.ui.opheld;

import org.apollo.game.message.impl.decode.ItemActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FourthOpHeldMessageDecoder extends MessageDecoder<ItemActionMessage> {
	@Override
	public ItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int itemId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int packedInterface = (int) reader.getUnsigned(DataType.INT);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);

		return new ItemActionMessage(4, packedInterface >> 16, packedInterface & 0xFFFF, itemId, slot);
	}
}
