package org.apollo.net.release.r377;

import org.apollo.game.message.impl.FourthNpcActionMessage;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.MessageDecoder;

/**
 * A {@link MessageDecoder} for the {@link FourthNpcActionMessage}.
 *
 * @author Stuart
 */
public final class FourthNpcActionMessageDecoder extends MessageDecoder<FourthNpcActionMessage> {

	@Override
	public FourthNpcActionMessage decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);
		int index = (int) reader.getUnsigned(DataType.SHORT);
		return new FourthNpcActionMessage(index);
	}

}