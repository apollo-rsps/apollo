package org.apollo.game.release.r181.decoders.ui.opheld;

import org.apollo.game.message.impl.decode.ItemActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * @author Khaled Abdeljaber
 */
public class FirstOpHeldMessageDecoder extends MessageDecoder<ItemActionMessage> {
	@Override
	public ItemActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int itemId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int packedInterface = (int) reader.getUnsigned(DataType.INT, DataOrder.MIDDLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT);

		return new ItemActionMessage(1, packedInterface >> 16, packedInterface & 0xFFFF, itemId, slot);
	}
}
