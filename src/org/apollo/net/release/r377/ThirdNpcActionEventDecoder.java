package org.apollo.net.release.r377;

import org.apollo.game.event.impl.ThirdNpcActionEvent;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * The {@link EventDecoder} for the {@link ThirdNpcActionEvent}.
 * 
 * @author Major
 */
public class ThirdNpcActionEventDecoder extends EventDecoder<ThirdNpcActionEvent> {

	@Override
	public ThirdNpcActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new ThirdNpcActionEvent(index);
	}

}