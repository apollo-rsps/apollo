package org.apollo.net.release.r317;

import org.apollo.game.event.impl.AddGlobalTileItemEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link AddGlobalTileItemEvent}.
 * 
 * @author Major
 */
public final class AddGlobalTileItemEventEncoder extends EventEncoder<AddGlobalTileItemEvent> {

	@Override
	public GamePacket encode(AddGlobalTileItemEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(215);
		builder.put(DataType.SHORT, DataTransformation.ADD, event.getId());
		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, event.getPositionOffset());
		builder.put(DataType.SHORT, DataTransformation.ADD, event.getIndex());
		builder.put(DataType.SHORT, event.getAmount());
		return builder.toGamePacket();
	}

}