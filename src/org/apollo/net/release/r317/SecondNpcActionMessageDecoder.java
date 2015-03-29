package org.apollo.net.release.r317;

import org.apollo.game.message.impl.SecondNpcActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link SecondNpcActionMessage}.
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