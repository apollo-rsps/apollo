package org.apollo.net.release.r317;

import org.apollo.game.message.impl.FirstNpcActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FirstNpcActionMessage}.
 * 
 * @author Major
 */
public final class FirstNpcActionMessageDecoder extends MessageDecoder<FirstNpcActionMessage> {

	@Override
	public FirstNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getSigned(DataType.SHORT, DataOrder.LITTLE);
		return new FirstNpcActionMessage(index);
	}

}