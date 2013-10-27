package org.apollo.net.release.r317;

import org.apollo.game.event.impl.SwitchItemEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link SwitchItemEvent}.
 * @author Graham
 */
public final class SwitchItemEventDecoder extends EventDecoder<SwitchItemEvent> {

	@Override
	public SwitchItemEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		boolean inserting = reader.getUnsigned(DataType.BYTE, DataTransformation.NEGATE) == 1;
		int oldSlot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int newSlot = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new SwitchItemEvent(interfaceId, inserting, oldSlot, newSlot);
	}

}
