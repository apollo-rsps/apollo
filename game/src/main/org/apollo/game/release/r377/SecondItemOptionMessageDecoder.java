package org.apollo.game.release.r377;

import org.apollo.game.message.impl.ItemOptionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the second {@link ItemOptionMessage}.
 *
 * @author Graham
 */
public final class SecondItemOptionMessageDecoder extends MessageDecoder<ItemOptionMessage> {

	@Override
	public ItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new ItemOptionMessage(2, interfaceId, id, slot);
	}

}