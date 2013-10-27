package org.apollo.net.release.r377;

import org.apollo.game.event.impl.EnteredAmountEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link EnteredAmountEvent}.
 * @author Graham
 */
public final class EnteredAmountEventDecoder extends EventDecoder<EnteredAmountEvent> {

	@Override
	public EnteredAmountEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int amount = (int) reader.getUnsigned(DataType.INT);
		return new EnteredAmountEvent(amount);
	}

}
