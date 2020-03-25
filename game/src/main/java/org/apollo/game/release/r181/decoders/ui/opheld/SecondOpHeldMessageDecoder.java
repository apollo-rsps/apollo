package org.apollo.game.release.r181.decoders.ui.opheld;

import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class SecondOpHeldMessageDecoder extends MessageDecoder<ItemActionMessage> {
	@Override
	public ItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int itemId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int interfacePacked = (int) reader.getUnsigned(DataType.INT, DataOrder.INVERSED_MIDDLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);

		return new ItemActionMessage(2, interfacePacked >> 16, interfacePacked & 0xFFFF, itemId, slot);
	}
}
