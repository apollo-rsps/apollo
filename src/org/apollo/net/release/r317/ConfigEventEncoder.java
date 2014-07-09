package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ConfigEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link ConfigEvent}.
 * 
 * @author Chris Fletcher
 * @author Major
 */
public final class ConfigEventEncoder extends EventEncoder<ConfigEvent> {

    @Override
    public GamePacket encode(ConfigEvent event) {
	GamePacketBuilder builder;
	int value = event.getValue();

	if (value > Byte.MIN_VALUE && value < Byte.MAX_VALUE) {
	    builder = new GamePacketBuilder(36);

	    builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
	    builder.put(DataType.BYTE, value & 0xFF);
	} else {
	    builder = new GamePacketBuilder(87);

	    builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
	    builder.put(DataType.INT, DataOrder.MIDDLE, value);
	}

	return builder.toGamePacket();
    }

}