package org.apollo.net.release.r377;

import org.apollo.game.event.impl.UpdateWeightEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link UpdateWeightEvent}.
 * 
 * @author Major
 */
public final class UpdateWeightEventEncoder extends EventEncoder<UpdateWeightEvent> {

    @Override
    public GamePacket encode(UpdateWeightEvent event) {
	GamePacketBuilder builder = new GamePacketBuilder(174);
	builder.put(DataType.SHORT, event.getWeight());
	return builder.toGamePacket();
    }

}