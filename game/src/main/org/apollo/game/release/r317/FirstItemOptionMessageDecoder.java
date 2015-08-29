package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ItemOptionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the first {@link ItemOptionMessage}.
 *
 * @author Graham
 */
public final class FirstItemOptionMessageDecoder extends MessageDecoder<ItemOptionMessage> {

	@Override
	public ItemOptionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);

		return new ItemOptionMessage(1, interfaceId, id, slot);
	}

}