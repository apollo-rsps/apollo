package org.apollo.net.release.r317;

import org.apollo.game.event.impl.AddTileItemEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link AddTileItemEvent}.
 * 
 * @author Major
 */
public final class AddTileItemEventEncoder extends EventEncoder<AddTileItemEvent> {

	@Override
	public GamePacket encode(AddTileItemEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(44);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getId());
		builder.put(DataType.SHORT, event.getAmount());
		builder.put(DataType.BYTE, event.getPositionOffset());
		return builder.toGamePacket();
	}

}