package org.apollo.game.release.r181.encoders.ui.container;

import org.apollo.game.message.impl.UpdateInventoryFullMessage;
import org.apollo.game.model.Item;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateInventoryFullMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class UpdateContainerFullMessageEncoder extends MessageEncoder<UpdateInventoryFullMessage> {

	@Override
	public GamePacket encode(UpdateInventoryFullMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(70, PacketType.VARIABLE_SHORT);

		Item[] items = message.getItems();

		builder.put(DataType.INT, message.getInterfaceId() << 16 | message.getComponent());
		builder.put(DataType.SHORT, message.getInventory());
		builder.put(DataType.SHORT, items.length);

		for (Item item : items) {
			int id = item == null ? -1 : item.getId();
			int amount = item == null ? 0 : item.getAmount();

			builder.put(DataType.SHORT, id + 1);

			if (amount > 254) {
				builder.put(DataType.BYTE, 0xFF);
				builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, amount);
			} else {
				builder.put(DataType.BYTE, amount);
			}
		}

		return builder.toGamePacket();
	}

}