package org.apollo.net.release.r377;

import org.apollo.game.message.impl.SecondNpcActionMessage;
import org.apollo.net.codec.game.*;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the {@link SecondNpcActionMessage}.
 * 
 * @author Major
 */
public final class SecondNpcActionMessageDecoder extends MessageDecoder<SecondNpcActionMessage> {

	@Override
	public SecondNpcActionMessage decode(GamePacket packet) {
        GamePacketReader reader = new GamePacketReader(packet);
        int index = (int) reader.getSigned(DataType.SHORT, DataTransformation.ADD);
        return new SecondNpcActionMessage(index);
	}

}