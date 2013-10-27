package org.apollo.net.release.r317;

import org.apollo.game.event.impl.ThirdItemActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ThirdItemActionEvent}.
 * @author Graham
 */
public final class ThirdItemActionEventDecoder extends EventDecoder<ThirdItemActionEvent> {

	@Override
	public ThirdItemActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new ThirdItemActionEvent(interfaceId, id, slot);
	}

}
