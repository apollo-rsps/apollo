package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetWidgetItemModelEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetItemModelEvent}.
 * 
 * @author Chris Fletcher
 */
final class SetWidgetItemModelEventEncoder extends EventEncoder<SetWidgetItemModelEvent> {

    @Override
    public GamePacket encode(SetWidgetItemModelEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(246);

	builder.put(DataType.SHORT, DataOrder.LITTLE, event.getInterfaceId());
	builder.put(DataType.SHORT, event.getZoom());
	builder.put(DataType.SHORT, event.getModelId());

	return builder.toGamePacket();
    }

}
