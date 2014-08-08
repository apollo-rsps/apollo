package org.apollo.net.release.r377;

import org.apollo.game.message.impl.ThirdNpcActionMessage;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * The {@link MessageDecoder} for the {@link ThirdNpcActionMessage}.
 * 
 * @author Major
 */
public final class ThirdNpcActionMessageDecoder extends MessageDecoder<ThirdNpcActionMessage> {

	@Override
	public ThirdNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT, DataOrder.LITTLE, DataTransformation.ADD);
		return new ThirdNpcActionMessage(index);
	}

}