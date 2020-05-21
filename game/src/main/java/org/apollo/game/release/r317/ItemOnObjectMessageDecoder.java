package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ItemOnObjectMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ItemOnObjectMessage}.
 *
 * @author Major
 */
public final class ItemOnObjectMessageDecoder extends MessageDecoder<ItemOnObjectMessage> {

	@Override
	public ItemOnObjectMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		int objectId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int y = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int x = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int itemId = (int) reader.getUnsigned(DataType.SHORT);

		return new ItemOnObjectMessage(interfaceId, itemId, slot, objectId, x, y);
	}

}