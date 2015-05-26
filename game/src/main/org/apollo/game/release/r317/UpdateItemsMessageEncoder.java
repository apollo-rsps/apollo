package org.apollo.game.release.r317;

import org.apollo.game.message.impl.UpdateItemsMessage;
import org.apollo.game.model.Item;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateItemsMessage}.
 *
 * @author Graham
 */
public final class UpdateItemsMessageEncoder extends MessageEncoder<UpdateItemsMessage> {

	@Override
	public GamePacket encode(UpdateItemsMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(53, PacketType.VARIABLE_SHORT);

		Item[] items = message.getItems();

		builder.put(DataType.SHORT, message.getInterfaceId());
		builder.put(DataType.SHORT, items.length);

		for (Item item : items) {
			int id = item == null ? -1 : item.getId();
			int amount = item == null ? 0 : item.getAmount();

			if (amount > 254) {
				builder.put(DataType.BYTE, 255);
				builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, amount);
			} else {
				builder.put(DataType.BYTE, amount);
			}

			builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, id + 1);
		}

		return builder.toGamePacket();
	}

}