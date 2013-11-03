package org.apollo.net.release.r377;

import org.apollo.game.event.impl.FirstNpcActionEvent;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * The {@link EventDecoder} for the {@link FirstNpcActionEvent}.
 * 
 * @author Major
 */
public class FirstNpcActionEventDecoder extends EventDecoder<FirstNpcActionEvent> {

	@Override
	public FirstNpcActionEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new FirstNpcActionEvent(index);
	}

}