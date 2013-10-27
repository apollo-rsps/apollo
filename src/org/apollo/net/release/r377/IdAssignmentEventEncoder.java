package org.apollo.net.release.r377;

import org.apollo.game.event.impl.IdAssignmentEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link IdAssignmentEvent}.
 * @author Graham
 */
public final class IdAssignmentEventEncoder extends EventEncoder<IdAssignmentEvent> {

	@Override
	public GamePacket encode(IdAssignmentEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(126);
		builder.put(DataType.BYTE, event.isMembers() ? 1 : 0);
		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getId());
		return builder.toGamePacket();
	}

}
