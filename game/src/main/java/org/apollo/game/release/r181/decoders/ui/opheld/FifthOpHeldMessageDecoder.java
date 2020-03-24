package org.apollo.game.release.r181.decoders.ui.opheld;

import org.apollo.game.message.impl.ItemActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FifthOpHeldMessageDecoder extends MessageDecoder<ItemActionMessage> {
	@Override
	public ItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfacePacked = (int) reader.getUnsigned(DataType.INT);
		int itemId = (int) reader.getUnsigned(DataType.SHORT);
		int slot = (int) reader.getUnsigned(DataType.SHORT);

		return new ItemActionMessage(5, interfacePacked >> 16, interfacePacked & 0xFFFF, itemId, slot);
	}
}
