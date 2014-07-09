package org.apollo.net.release.r377;

import org.apollo.game.event.impl.AddGlobalTileItemEvent;
import org.apollo.net.codec.game.DataOrder;
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
	GamePacketBuilder builder = new GamePacketBuilder(106);
	builder.put(DataType.BYTE, DataTransformation.ADD, event.getPositionOffset());
	builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getAmount());
	builder.put(DataType.SHORT, DataTransformation.ADD, event.getId());
	builder.put(DataType.SHORT, DataTransformation.ADD, event.getIndex());
	return builder.toGamePacket();
    }

}