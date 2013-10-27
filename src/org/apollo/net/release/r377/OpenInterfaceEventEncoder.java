package org.apollo.net.release.r377;

import org.apollo.game.event.impl.OpenInterfaceEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link OpenInterfaceEvent}.
 * @author Graham
 */
public final class OpenInterfaceEventEncoder extends EventEncoder<OpenInterfaceEvent> {

	@Override
	public GamePacket encode(OpenInterfaceEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(159);
		builder.put(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD, event.getId());
		return builder.toGamePacket();
	}

}
