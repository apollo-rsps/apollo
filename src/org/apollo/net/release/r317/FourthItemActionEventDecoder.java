package org.apollo.net.release.r317;

import org.apollo.game.event.impl.FourthItemActionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link FourthItemActionEvent}.
 * @author Graham
 */
public final class FourthItemActionEventDecoder extends EventDecoder<FourthItemActionEvent> {

	@Override
	public FourthItemActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int slot = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		int interfaceId = (int) reader.getUnsigned(DataType.SHORT);
		int id = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new FourthItemActionEvent(interfaceId, id, slot);
	}

}
