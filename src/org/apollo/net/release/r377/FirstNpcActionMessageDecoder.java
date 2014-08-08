package org.apollo.net.release.r377;

import org.apollo.game.message.impl.FirstNpcActionMessage;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the {@link FirstNpcActionMessage}.
 * 
 * @author Major
 */
public final class FirstNpcActionMessageDecoder extends MessageDecoder<FirstNpcActionMessage> {

	@Override
	public FirstNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataTransformation.ADD);
		return new FirstNpcActionMessage(index);
	}

}