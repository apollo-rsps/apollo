package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ThirdItemOptionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ThirdItemOptionEvent}.
 * @author Chris Fletcher
 */
final class ThirdItemOptionEventDecoder extends EventDecoder<ThirdItemOptionEvent> {

	@Override
	public ThirdItemOptionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int interfaceId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);

		return new ThirdItemOptionEvent(interfaceId, id, slot);
	}

}
