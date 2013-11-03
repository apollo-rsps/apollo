package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FifthItemOptionEvent;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FifthItemOptionEvent}.
 * @author Chris Fletcher
 */
final class FifthItemOptionEventDecoder extends EventDecoder<FifthItemOptionEvent> {

	@Override
	public FifthItemOptionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		int slot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);

		return new FifthItemOptionEvent(interfaceId, id, slot);
	}

}
