package org.apollo.net.release.r317;

import org.apollo.game.event.impl.DisplayTabInterfaceEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link DisplayTabInterfaceEvent}.
 * 
 * @author Chris Fletcher
 */
final class DisplayTabInterfaceEventEncoder extends EventEncoder<DisplayTabInterfaceEvent> {

	@Override
	public GamePacket encode(DisplayTabInterfaceEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(106);
		builder.put(DataType.BYTE, DataTransformation.NEGATE, event.getTab());
		return builder.toGamePacket();
	}

}