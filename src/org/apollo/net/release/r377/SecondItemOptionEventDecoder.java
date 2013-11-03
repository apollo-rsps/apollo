package org.apollo.net.release.r377;

import org.apollo.game.event.impl.SecondItemOptionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link SecondItemOptionEvent}.
 * @author Graham
 */
final class SecondItemOptionEventDecoder extends EventDecoder<SecondItemOptionEvent> {

	@Override
	public SecondItemOptionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);

		return new SecondItemOptionEvent(interfaceId, id, slot);
	}

}
