package org.apollo.net.release.r377;

import org.apollo.game.event.impl.ConfigEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
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
final class ConfigEventEncoder extends EventEncoder<ConfigEvent> {

    @Override
    public GamePacket encode(ConfigEvent event) {
	GamePacketBuilder builder;
	int value = event.getValue();

	if (value > Byte.MIN_VALUE && value < Byte.MAX_VALUE) {
	    builder = new GamePacketBuilder(182);

	    builder.put(DataType.SHORT, DataTransformation.ADD, event.getId());
	    builder.put(DataType.BYTE, DataTransformation.SUBTRACT, value & 0xFF);
	} else {
	    builder = new GamePacketBuilder(115);

	    builder.put(DataType.INT, DataOrder.INVERSED_MIDDLE, value);
	    builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
	}

	return builder.toGamePacket();
    }

}