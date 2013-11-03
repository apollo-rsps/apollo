package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FirstItemOptionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FirstItemOptionEvent}.
 * @author Chris Fletcher
 */
final class FirstItemOptionEventDecoder extends EventDecoder<FirstItemOptionEvent> {

	@Override
	public FirstItemOptionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);

		return new FirstItemOptionEvent(interfaceId, id, slot);
	}

}
