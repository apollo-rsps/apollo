package org.apollo.net.release.r377;

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
	GamePacketBuilder builder = new GamePacketBuilder(208);
	builder.put(DataType.SHORT, DataTransformation.ADD, event.getId());
	builder.put(DataType.BYTE, DataTransformation.ADD, event.getPositionOffset());
	return builder.toGamePacket();
    }

}