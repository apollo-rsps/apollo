package org.apollo.net.release.r317;

import org.apollo.game.event.impl.OpenDialogueInterfaceEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link OpenDialogueInterfaceEvent}.
 * 
 * @author Chris Fletcher
 */
public final class OpenDialogueInterfaceEventEncoder extends EventEncoder<OpenDialogueInterfaceEvent> {

	@Override
	public GamePacket encode(OpenDialogueInterfaceEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(164);
		builder.put(DataType.SHORT, DataOrder.LITTLE, event.getInterfaceId());
		return builder.toGamePacket();
	}

}