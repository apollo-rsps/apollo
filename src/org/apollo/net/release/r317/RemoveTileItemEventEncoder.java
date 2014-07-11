package org.apollo.net.release.r317;

import org.apollo.game.event.impl.RemoveTileItemEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link RemoveTileItemEvent}.
 * 
 * @author Major
 */
public final class RemoveTileItemEventEncoder extends EventEncoder<RemoveTileItemEvent> {

	@Override
	public GamePacket encode(RemoveTileItemEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(156);
		builder.put(DataType.BYTE, DataTransformation.ADD, event.getPositionOffset());
		builder.put(DataType.SHORT, event.getId());
		return builder.toGamePacket();
	}

}