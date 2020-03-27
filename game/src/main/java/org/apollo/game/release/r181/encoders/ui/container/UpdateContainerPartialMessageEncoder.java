package org.apollo.game.release.r181.encoders.ui.container;

import org.apollo.game.message.impl.encode.UpdateInventoryPartialMessage;
import org.apollo.game.model.Item;
import org.apollo.game.model.inv.SlottedItem;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link UpdateInventoryPartialMessage}.
 *
 * @author Khaled Abdeljaber
 */
public final class UpdateContainerPartialMessageEncoder extends MessageEncoder<UpdateInventoryPartialMessage> {

	@Override
	public GamePacket encode(UpdateInventoryPartialMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(44, PacketType.VARIABLE_SHORT);
		SlottedItem[] items = message.getSlottedItems();

		builder.put(DataType.INT, message.getInterfaceId() << 16 | message.getComponent());
		builder.put(DataType.SHORT, message.getContainerId());

		for (SlottedItem slottedItem : items) {
			builder.putSmart(slottedItem.getSlot());

			Item item = slottedItem.getItem();
			int id = item == null ? -1 : item.getId();
			int amount = item == null ? 0 : item.getAmount();

			builder.put(DataType.SHORT, id + 1);

			if (amount > 254) {
				builder.put(DataType.BYTE, 255);
				builder.put(DataType.INT, amount);
			} else {
				builder.put(DataType.BYTE, amount);
			}
		}

		return builder.toGamePacket();
	}
}
