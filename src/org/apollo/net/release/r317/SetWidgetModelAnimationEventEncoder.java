package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SetWidgetModelAnimationEvent;
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
	GamePacketBuilder builder = new GamePacketBuilder(200);

	builder.put(DataType.SHORT, event.getInterfaceId() & 0xFFFF);
	builder.put(DataType.SHORT, event.getAnimation());

	return builder.toGamePacket();
    }

}