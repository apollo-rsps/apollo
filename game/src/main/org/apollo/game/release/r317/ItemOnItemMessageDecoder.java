package org.apollo.game.release.r317;

import org.apollo.game.message.impl.ItemOnItemMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link ItemOnItemMessageDecoder}.
 *
 * @author Chris Fletcher
 */
public final class ItemOnItemMessageDecoder extends MessageDecoder<ItemOnItemMessage> {

	@Override
	public ItemOnItemMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int targetSlot = Math.toIntExact(reader.getUnsigned(DataType.SHORT));
		int usedSlot = Math.toIntExact(reader.getUnsigned(DataType.SHORT, DataTransformation.ADD));

		int targetId = Math.toIntExact(reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD));
		int targetInterface = Math.toIntExact(reader.getUnsigned(DataType.SHORT));

		int usedId = Math.toIntExact(reader.getSigned(DataType.SHORT, DataOrder.LITTLE));
		int usedInterface = Math.toIntExact(reader.getUnsigned(DataType.SHORT));

		return new ItemOnItemMessage(usedInterface, usedId, usedSlot, targetInterface, targetId, targetSlot);
	}

}