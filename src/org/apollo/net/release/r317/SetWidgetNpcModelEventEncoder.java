package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetWidgetNpcModelEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetNpcModelEvent}.
 * 
 * @author Chris Fletcher
 */
final class SetWidgetNpcModelEventEncoder extends EventEncoder<SetWidgetNpcModelEvent> {

    @Override
    public GamePacket encode(SetWidgetNpcModelEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(75);

	builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getModelId());
	builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());

	return builder.toGamePacket();
    }

}
