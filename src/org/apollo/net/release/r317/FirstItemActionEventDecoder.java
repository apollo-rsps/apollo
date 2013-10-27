package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FirstItemActionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FirstItemActionEvent}.
 * @author Graham
 */
public final class FirstItemActionEventDecoder extends EventDecoder<FirstItemActionEvent> {

	@Override
	public FirstItemActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new FirstItemActionEvent(interfaceId, id, slot);
	}

}
