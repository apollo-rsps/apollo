package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FirstItemOptionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FirstItemOptionEvent}.
 * @author Graham
 */
final class FirstItemOptionEventDecoder extends EventDecoder<FirstItemOptionEvent> {

	@Override
	public FirstItemOptionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int interfaceId = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);

		return new FirstItemOptionEvent(interfaceId, id, slot);
	}

}
