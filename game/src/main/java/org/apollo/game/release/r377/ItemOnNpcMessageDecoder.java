package org.apollo.game.release.r377;

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
 * @author Major
 */
public final class ItemOnNpcMessageDecoder extends MessageDecoder<ItemOnNpcMessage> {

	@Override
	public ItemOnNpcMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int index = (int) reader.getUnsigned(DataType.SHORT);
		int id = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		int widget = (int) reader.getUnsigned(DataType.SHORT,  DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT);

		return new ItemOnNpcMessage(id, index, slot, widget);
	}

}
