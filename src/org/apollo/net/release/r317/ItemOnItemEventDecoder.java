package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ItemOnItemEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ItemOnItemEventDecoder}.
 * 
 * @author Chris Fletcher
 */
final class ItemOnItemEventDecoder extends EventDecoder<ItemOnItemEvent> {

	@Override
	public ItemOnItemEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int targetSlot = (int) reader.getUnsigned(DataType.SHORT);
		int usedSlot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		int targetId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int targetInterface = (int) reader.getUnsigned(DataType.SHORT);

		int usedId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		int usedInterface = (int) reader.getUnsigned(DataType.SHORT);

		return new ItemOnItemEvent(usedInterface, usedId, usedSlot, targetInterface, targetId, targetSlot);
	}

}