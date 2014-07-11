package org.apollo.net.release.r377;

import org.apollo.game.event.impl.AddTileItemEvent;
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
		GamePacketBuilder builder = new GamePacketBuilder(107);
		builder.put(DataType.SHORT, event.getId());
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getPositionOffset());
		builder.put(DataType.SHORT, DataTransformation.ADD, event.getAmount());
		return builder.toGamePacket();
	}

}