package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ItemOnNpcMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ItemOnNpcMessage}.
 *
 * @author Lmctruck30
 */
public final class ItemOnNpcMessageDecoder extends MessageDecoder<ItemOnNpcMessage> {

	@Override
	public ItemOnNpcMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int itemId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int npcIndex = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int itemSlot = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new ItemOnNpcMessage(itemId, npcIndex, itemSlot, interfaceId);
	}

}