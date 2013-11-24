package org.apollo.net.release.r377;

import org.apollo.game.event.impl.ArrowKeyEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for the {@link ArrowKeyEvent}.
 * 
 * @author Major
 */
public class ArrowKeyEventDecoder extends EventDecoder<ArrowKeyEvent> {

	@Override
	public ArrowKeyEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int roll = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		int yaw = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE);
		return new ArrowKeyEvent(roll, yaw);
	}

}