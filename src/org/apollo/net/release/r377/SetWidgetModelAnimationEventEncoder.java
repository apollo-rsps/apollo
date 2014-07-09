package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SetWidgetModelAnimationEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link SetWidgetModelAnimationEvent}.
 * 
 * @author Chris Fletcher
 */
final class SetWidgetModelAnimationEventEncoder extends EventEncoder<SetWidgetModelAnimationEvent> {

    @Override
    public GamePacket encode(SetWidgetModelAnimationEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(2);

	builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getInterfaceId());
	builder.put(DataType.SHORT, DataTransformation.ADD, event.getAnimation() & 0xFFFF);

	return builder.toGamePacket();
    }

}