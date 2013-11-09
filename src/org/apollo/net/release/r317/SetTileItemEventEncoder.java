package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetTileItemEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetTileItemEvent}.
 * 
 * @author Major
 */
public class SetTileItemEventEncoder extends EventEncoder<SetTileItemEvent> {

	@Override
	public GamePacket encode(SetTileItemEvent event) {
		GamePacketBuilder builder = null;
		if (event.getAmount() == 0) { // remove the item.
			builder = new GamePacketBuilder(156);

			builder.put(DataType.BYTE, DataTransformation.ADD, event.getPositionOffset());
			builder.put(DataType.SHORT, event.getId());
		} else if (!event.isUpdating()) { // sending a new item
			builder = new GamePacketBuilder(44);

			builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getId());
			builder.put(DataType.SHORT, event.getAmount());
			builder.put(DataType.BYTE, event.getPositionOffset());
		} else { // updating an already displayed item
			builder = new GamePacketBuilder(84);

			builder.put(DataType.BYTE, event.getPositionOffset());
			builder.put(DataType.SHORT, event.getId());
			builder.put(DataType.SHORT, event.getPreviousAmount());
			builder.put(DataType.SHORT, event.getAmount());
		}
		return builder.toGamePacket();
	}

}